import egreso.Egreso;
import egreso.Etiqueta;
import egreso.Item;
import entidadOrganizativa.*;
import entidadOrganizativa.exceptions.EntidadBaseNoIncorporableException;
import entidadOrganizativa.exceptions.EntidadSinEntidadesBaseException;
import entidadOrganizativa.exceptions.MontoSuperadoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class EntidadTest {

    private Organizacion organizacion;
    private EntidadJuridica entidadJuridica;
    private EntidadBase entidadBase;
    private Egreso egreso1;
    private Egreso egreso2;
    private Item item1;
    private Item item2;
    private Item item3;

    @Before
    public void objetosTest() {
        organizacion = new Organizacion(new ArrayList<>(),new ArrayList<>());
        entidadJuridica = new OrganizacionSectorSocial(null,null,156,null,new ArrayList<>());
        entidadBase = new EntidadBase(null,null,new ArrayList<>());
        egreso1 = new Egreso(null, null, new ArrayList<>(),null,null);
        egreso2 = new Egreso(null, null, new ArrayList<>(),null,null);
        item1 = new Item(null, new BigDecimal("100"), 1, null);
        item2 = new Item(null, new BigDecimal("200"), 1, null);
        item3 = new Item(null, new BigDecimal("300"), 1, null);
        egreso1.getItems().add(item1);
        egreso1.getItems().add(item2);
        egreso1.getItems().add(item3);
        egreso2.getItems().add(item1);
        egreso2.getItems().add(item2);
        egreso2.getItems().add(item3);
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
        CategoriaEntidad categoriaEntidad = new CategoriaEntidad("ONG",BigDecimal.valueOf(1000), Arrays.asList(Regla.BLOQUEO_EGRESOS_POR_MONTO));
        organizacion.agregarCategoria(categoriaEntidad);
        organizacion.agregarEntidad(entidadBase);
        organizacion.configurarCategoriaAEntidad(entidadBase,categoriaEntidad);
        entidadBase.agregarEgreso(egreso1);
        entidadBase.agregarEgreso(egreso2);
    }

    @Test(expected = EntidadSinEntidadesBaseException.class)
    public void entidadJuridicaNoPuedeAgregarEntidadesBase() {
        CategoriaEntidad categoriaEntidad = new CategoriaEntidad("Agro",null, Arrays.asList(Regla.ENTIDAD_JURIDICA_SIN_ENTIDADES_BASE));
        organizacion.agregarCategoria(categoriaEntidad);
        organizacion.agregarEntidad(entidadJuridica);
        organizacion.configurarCategoriaAEntidad(entidadJuridica,categoriaEntidad);
        entidadJuridica.agregarEntidadBase(entidadBase);
    }

    @Test(expected = EntidadBaseNoIncorporableException.class)
    public void entidadBaseNoEsIncorporableAEntidadJuridica() {
        CategoriaEntidad categoriaEntidad = new CategoriaEntidad("Pyme",null, Arrays.asList(Regla.ENTIDAD_BASE_NO_INCORPORABLE));
        organizacion.agregarCategoria(categoriaEntidad);
        organizacion.agregarEntidad(entidadJuridica);
        organizacion.agregarEntidad(entidadBase);
        organizacion.configurarCategoriaAEntidad(entidadBase,categoriaEntidad);
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

}
