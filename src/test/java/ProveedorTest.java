import apiMercadoLibre.DTO.MonedaDTO;
import apiMercadoLibre.ValidadorDeMoneda;
import apiMercadoLibre.exceptions.DireccionInvalidaException;
import egreso.Egreso;
import entidadOrganizativa.Entidad;
import entidadOrganizativa.EntidadBase;
import entidadOrganizativa.Organizacion;
import entidadOrganizativa.RepositorioDeOrganizaciones;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import apiMercadoLibre.DTO.CiudadDTO;
import apiMercadoLibre.DTO.PaisDTO;
import apiMercadoLibre.DTO.ProvinciaDTO;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import presupuesto.DireccionPostal;
import presupuesto.Presupuesto;
import presupuesto.Proveedor;
import apiMercadoLibre.ServiceLocator;
import apiMercadoLibre.ValidadorDeUbicacion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class ProveedorTest extends AbstractPersistenceTest implements WithGlobalEntityManager, TransactionalOps {

    @Before
    public void objetosTest() {
        MonedaDTO pesoArgentino = new MonedaDTO(null,null,"Peso argentino");
        ValidadorDeMoneda validadorDeMoneda = new ValidadorDeMoneda(Arrays.asList(pesoArgentino));
        ServiceLocator.getInstance().setValidadorDeMoneda(validadorDeMoneda);
        CiudadDTO ciudad = new CiudadDTO(null,"La Plata");
        ProvinciaDTO provincia = new ProvinciaDTO(null,"Buenos Aires", Arrays.asList(ciudad));
        PaisDTO pais = new PaisDTO(null,"Argentina", Arrays.asList(provincia));
        ValidadorDeUbicacion validadorDeUbicacion = new ValidadorDeUbicacion(Arrays.asList(pais));
        ServiceLocator.getInstance().setValidadorDeUbicacion(validadorDeUbicacion);
    }

    @Test
    public void crearProveedor() {
        Proveedor proveedor = new Proveedor("Carlitos SRL", "20-40131392-4", null);
        Assert.assertNotNull(proveedor);
    }

    @Test
    public void crearProveedorConDireccionPostalValida() {
        Proveedor proveedor = new Proveedor(null,null,new DireccionPostal("Argentina","Buenos Aires","La Plata",null));
    }

    @Test(expected = DireccionInvalidaException.class)
    public void crearProveedorConDireccionPostalErronea() {
        Proveedor proveedor = new Proveedor(null,null,new DireccionPostal("Argentina","Chubut","Biedma",null));
    }

    @Test
    public void recuperoProveedorConDireccionPostalValidaDeLaBase() throws Exception {
        Organizacion organizacion = new Organizacion(new ArrayList<>(), null);
        EntidadBase entidadBase = new EntidadBase(null,null,new ArrayList<>());
        Egreso egreso = new Egreso(null, null,null,null,"Peso argentino");
        Proveedor proveedor = new Proveedor(null,null,new DireccionPostal("Argentina","Buenos Aires","La Plata",null));
        Presupuesto presupuesto = new Presupuesto(null,null,egreso,"Peso argentino",proveedor);
        organizacion.agregarEntidad(entidadBase);
        entidadBase.agregarEgreso(egreso);
        withTransaction(() -> RepositorioDeOrganizaciones.getInstance().agregar(organizacion));
        entityManager().clear();
        Optional<Organizacion> organizacionMisteriosa = RepositorioDeOrganizaciones.getInstance().getPorId(organizacion.getId());
        Assert.assertTrue(organizacionMisteriosa.isPresent());
        organizacionMisteriosa.ifPresent( o -> {
            Entidad entidadMisteriosa = o.getEntidades().stream().filter(e -> e.getId().equals(entidadBase.getId())).findFirst().get();
            Egreso egresoMisterioso = entidadMisteriosa.getEgresos().stream().filter(eg -> eg.getId().equals(egreso.getId())).findFirst().get();
            Presupuesto presupuestoMisterioso = egresoMisterioso.getPresupuestos().stream().filter(p -> p.getId().equals(presupuesto.getId())).findFirst().get();
            Assert.assertEquals("Argentina",presupuestoMisterioso.getProveedor().getDireccionPostal().getPais());
            withTransaction(() -> RepositorioDeOrganizaciones.getInstance().borrar(o));
        });
    }
}
