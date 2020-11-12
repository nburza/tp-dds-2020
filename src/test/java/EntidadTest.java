import egreso.Egreso;
import egreso.Etiqueta;
import egreso.Item;
import egreso.Producto;
import entidadOrganizativa.*;
import entidadOrganizativa.exceptions.EntidadBaseNoIncorporableException;
import entidadOrganizativa.exceptions.EntidadSinEntidadesBaseException;
import entidadOrganizativa.exceptions.MontoSuperadoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import apiMercadoLibre.DTO.MonedaDTO;
import apiMercadoLibre.ServiceLocator;
import apiMercadoLibre.ValidadorDeMoneda;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class EntidadTest extends AbstractPersistenceTest implements WithGlobalEntityManager, TransactionalOps{

    private Organizacion organizacion;
    private EntidadJuridica entidadJuridica;
    private EntidadBase entidadBase;
    private Egreso egreso1;
    private Egreso egreso2;
    private Egreso egreso3;
    private Etiqueta etiqueta1;
    private Etiqueta etiqueta2;
    private Etiqueta etiqueta3;
    private Producto producto1;
    private Producto producto2;
    private Producto producto3;
    private Item item1;
    private Item item2;
    private Item item3;

    @Before
    public void objetosTest() {
        MonedaDTO pesoArgentino = new MonedaDTO(null,null,"Peso argentino");
        ValidadorDeMoneda validadorDeMoneda = new ValidadorDeMoneda(Arrays.asList(pesoArgentino));
        ServiceLocator.getInstance().setValidadorDeMoneda(validadorDeMoneda);
        organizacion = new Organizacion(new ArrayList<>(),new ArrayList<>());
        entidadJuridica = new OrganizacionSectorSocial(null,null,156,null,new ArrayList<>());
        entidadBase = new EntidadBase(null,null,new ArrayList<>());
        egreso1 = new Egreso(null, null, new ArrayList<>(),null,"Peso argentino");
        egreso2 = new Egreso(null, null, new ArrayList<>(),null,"Peso argentino");
        egreso3 = new Egreso(null, null, new ArrayList<>(),LocalDate.now().minusDays(60),"Peso argentino");
        producto1 = new Producto(null);
        producto2 = new Producto(null);
        producto3 = new Producto(null);
        item1 = new Item(producto1, 1, "Peso argentino", new BigDecimal("100"));
        item2 = new Item(producto2, 1, "Peso argentino", new BigDecimal("200"));
        item3 = new Item(producto3, 1, "Peso argentino", new BigDecimal("300"));
        egreso1.getItems().add(item1);
        egreso1.getItems().add(item2);
        egreso1.getItems().add(item3);
        egreso2.getItems().add(item1);
        egreso2.getItems().add(item2);
        egreso2.getItems().add(item3);
        egreso3.getItems().add(item1);
        etiqueta1 = new Etiqueta("Indumentaria");
        etiqueta2 = new Etiqueta("Amoblamiento");
        etiqueta3 = new Etiqueta("Amoblamiento");
        egreso1.agregarEtiqueta(etiqueta1);
        egreso1.agregarEtiqueta(etiqueta2);
        egreso2.agregarEtiqueta(etiqueta1);
        egreso3.agregarEtiqueta(etiqueta1);
    }

    @Test
    public void totalEgresosDeUnaEntidadBase() {
        entidadBase.agregarEgreso(egreso1);
        Assert.assertEquals(BigDecimal.valueOf(600),entidadBase.totalEgresos());
    }

    @Test
    public void totalEgresosDeUnaEntidadJuridicaSinEntidadesBase() {
        entidadJuridica.agregarEgreso(egreso1);
        Assert.assertEquals(BigDecimal.valueOf(600),entidadJuridica.totalEgresos());
    }

    @Test
    public void totalEgresosDeUnaEntidadJuridicaConEntidadesBase() {
        entidadJuridica.agregarEntidadBase(entidadBase);
        entidadJuridica.agregarEgreso(egreso1);
        entidadBase.agregarEgreso(egreso2);
        Assert.assertEquals(BigDecimal.valueOf(1200),entidadJuridica.totalEgresos());
    }

    @Test(expected = MontoSuperadoException.class)
    public void bloqueoEgresoPorMontoLimite() {

        CategoriaEntidad categoriaEntidad = new CategoriaEntidad("ONG");
        categoriaEntidad.agregarRegla(new ReglaBloqueoEgresoPorMonto(BigDecimal.valueOf(1000)));
        organizacion.agregarCategoria(categoriaEntidad);
        organizacion.agregarEntidad(entidadBase);
        organizacion.agregarCategoriaAEntidad(entidadBase,categoriaEntidad);
        entidadBase.agregarEgreso(egreso1);
        entidadBase.agregarEgreso(egreso2);

    }

    @Test(expected = EntidadSinEntidadesBaseException.class)
    public void entidadJuridicaNoPuedeAgregarEntidadesBase() {

        CategoriaEntidad categoriaEntidad=new CategoriaEntidad("Agro");
        categoriaEntidad.agregarRegla(new ReglaProhibidoAgregarEntidadesBase());
        organizacion.agregarCategoria(categoriaEntidad);
        organizacion.agregarEntidad(entidadJuridica);
        organizacion.agregarCategoriaAEntidad(entidadJuridica,categoriaEntidad);
        entidadJuridica.agregarEntidadBase(entidadBase);
    }

    @Test(expected = EntidadBaseNoIncorporableException.class)
    public void entidadBaseNoEsIncorporableAEntidadJuridica() {
       CategoriaEntidad categoriaEntidad = new CategoriaEntidad("Pyme");
       categoriaEntidad.agregarRegla(new ReglaEntidadBaseNoIncorporable());
        organizacion.agregarCategoria(categoriaEntidad);
        organizacion.agregarEntidad(entidadJuridica);
        organizacion.agregarEntidad(entidadBase);
        organizacion.agregarCategoriaAEntidad(entidadBase,categoriaEntidad);
        entidadJuridica.agregarEntidadBase(entidadBase);
    }

    @Test
    public void totalEgresosConEtiqueta(){
        Etiqueta etiqueta1 = new Etiqueta("Indumentaria");
        egreso1.agregarEtiqueta(etiqueta1);
        egreso2.agregarEtiqueta(etiqueta1);
        entidadJuridica.agregarEgreso(egreso1);
        entidadJuridica.agregarEgreso(egreso2);
        Assert.assertEquals(BigDecimal.valueOf(1200),entidadJuridica.gastosTotalPorEtiqueta(etiqueta1, entidadJuridica.getEgresos()));
    }

    @Test
    public void reporteGastoSMensual(){
        entidadJuridica.agregarEgreso(egreso1);
        entidadJuridica.agregarEgreso(egreso2);
        Hashtable<Etiqueta,BigDecimal> totalPorEtiqueta = new Hashtable<>();
        totalPorEtiqueta.put(etiqueta1, BigDecimal.valueOf(1200));
        totalPorEtiqueta.put(etiqueta2, BigDecimal.valueOf(600));
        Assert.assertEquals(totalPorEtiqueta,entidadJuridica.reporteMensualGastosPorEtiqueta());
    }

    @Test
    public void reporteGastoMensualConEgresosViejos(){
        entidadJuridica.agregarEgreso(egreso1);
        entidadJuridica.agregarEgreso(egreso2);
        entidadJuridica.agregarEgreso(egreso3);
        Hashtable<Etiqueta,BigDecimal> totalPorEtiqueta = new Hashtable<>();
        totalPorEtiqueta.put(etiqueta1, BigDecimal.valueOf(1200));
        totalPorEtiqueta.put(etiqueta2, BigDecimal.valueOf(600));
        Assert.assertEquals(totalPorEtiqueta,entidadJuridica.reporteMensualGastosPorEtiqueta());
    }

    @Test
    public void reporteGastoMensualConEtiquetasRepetidas(){
        egreso1.agregarEtiqueta(etiqueta3);
        entidadJuridica.agregarEgreso(egreso1);
        entidadJuridica.agregarEgreso(egreso2);
        entidadJuridica.agregarEgreso(egreso3);
        Hashtable<Etiqueta,BigDecimal> totalPorEtiqueta = new Hashtable<>();
        totalPorEtiqueta.put(etiqueta1, BigDecimal.valueOf(1200));
        totalPorEtiqueta.put(etiqueta2, BigDecimal.valueOf(600));
        Assert.assertEquals(totalPorEtiqueta,entidadJuridica.reporteMensualGastosPorEtiqueta());
    }

    @Test
    public void recuperoOrganizacionConDosEntidades(){
        organizacion.agregarEntidad(entidadBase);
        organizacion.agregarEntidad(entidadJuridica);
        withTransaction(() -> RepositorioDeOrganizaciones.getInstance().agregar(organizacion));
        entityManager().clear();
        Optional<Organizacion> organizacionMisteriosa = RepositorioDeOrganizaciones.getInstance().getPorId(organizacion.getId());
        Assert.assertTrue(organizacionMisteriosa.isPresent());
        organizacionMisteriosa.ifPresent( o -> {
            Assert.assertEquals(2,o.getEntidades().size());
            withTransaction(() -> RepositorioDeOrganizaciones.getInstance().borrar(o));
        });
    }

    @Test
    public void recuperoEntidadConEgresos(){
        entidadJuridica.agregarEgreso(egreso1);
        organizacion.agregarEntidad(entidadJuridica);
        withTransaction(() -> RepositorioDeOrganizaciones.getInstance().agregar(organizacion));
        entityManager().clear();
        Optional<Organizacion> organizacionMisteriosa = RepositorioDeOrganizaciones.getInstance().getPorId(organizacion.getId());
        Assert.assertTrue(organizacionMisteriosa.isPresent());
        organizacionMisteriosa.ifPresent( o -> {
            Entidad entidadMisteriosa = o.getEntidades().stream().filter(e -> e.getId().equals(entidadJuridica.getId())).findFirst().get();
            Egreso egresoMisterioso = entidadMisteriosa.getEgresos().stream().filter(eg -> eg.getId().equals(egreso1.getId())).findFirst().get();
            Assert.assertEquals(3,egresoMisterioso.getItems().size());
            withTransaction(() -> RepositorioDeOrganizaciones.getInstance().borrar(o));
        });
    }

    @Test
    public void calculoTotalDeEgresosDeUnaEntidadSinEgresos(){
        Entidad entidad = new EntidadBase(null,null,new ArrayList<>());
        Assert.assertEquals(BigDecimal.valueOf(0),entidad.totalEgresos());
    }
}
