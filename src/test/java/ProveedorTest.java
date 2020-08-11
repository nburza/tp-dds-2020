import apiMercadoLibre.exceptions.DireccionInvalidaException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import apiMercadoLibre.DTO.CiudadDTO;
import apiMercadoLibre.DTO.PaisDTO;
import apiMercadoLibre.DTO.ProvinciaDTO;
import presupuesto.DireccionPostal;
import presupuesto.Proveedor;
import apiMercadoLibre.ServiceLocator;
import apiMercadoLibre.ValidadorDeUbicacion;

import java.util.Arrays;

public class ProveedorTest {

    @Before
    public void objetosTest() {
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
}
