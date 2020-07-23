import org.junit.Assert;
import org.junit.Test;
import proveedor.*;
import proveedor.DTO.*;

import java.util.List;

public class ServicioUbicacionTest {

    @Test
    public void getPaisesRetornaListaNoVacia() {
        List<PaisDTO> paises = new ServicioUbicacionMercadoLibre().getPaises();
        Assert.assertFalse(paises.isEmpty());
        PaisDTO primerPais = paises.get(0);
        Assert.assertEquals(primerPais.getName(),"Argentina");
    }

    @Test
    public void getProvinciasRetornaListaNoVacia() {
        PaisDTO argentina = new PaisDTO("AR","Argentina");
        List<ProvinciaDTO> provincias = new ServicioUbicacionMercadoLibre().getProvincias(argentina);
        Assert.assertFalse(provincias.isEmpty());
    }

    @Test
    public void getCiudadesRetornaListaNoVacia() {
        ProvinciaDTO catamarca = new ProvinciaDTO("TUxBUENBVGFiY2Fm","Catamarca");
        List<CiudadDTO> ciudades = new ServicioUbicacionMercadoLibre().getCiudades(catamarca);
        Assert.assertFalse(ciudades.isEmpty());
    }

    @Test
    public void getMonedasRetornaListaNoVacia() {
        List<MonedaDTO> monedas = new ServicioUbicacionMercadoLibre().getMonedas();
        Assert.assertFalse(monedas.isEmpty());
    }
}
