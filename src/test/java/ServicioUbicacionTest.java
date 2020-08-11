import org.junit.Assert;
import org.junit.Test;
import apiMercadoLibre.*;
import apiMercadoLibre.DTO.*;

import java.util.List;

public class ServicioUbicacionTest {

    @Test
    public void getPaisesRetornaListaNoVacia() {
        List<PaisDTO> paises = new ServicioAPIMercadoLibre().getPaises();
        Assert.assertFalse(paises.isEmpty());
        PaisDTO primerPais = paises.get(0);
        Assert.assertEquals(primerPais.getName(),"Argentina");
    }

    @Test
    public void getProvinciasRetornaListaNoVacia() {
        PaisDTO argentina = new PaisDTO("AR","Argentina");
        List<ProvinciaDTO> provincias = new ServicioAPIMercadoLibre().getProvincias(argentina);
        Assert.assertFalse(provincias.isEmpty());
    }

    @Test
    public void getCiudadesRetornaListaNoVacia() {
        ProvinciaDTO catamarca = new ProvinciaDTO("TUxBUENBVGFiY2Fm","Catamarca");
        List<CiudadDTO> ciudades = new ServicioAPIMercadoLibre().getCiudades(catamarca);
        Assert.assertFalse(ciudades.isEmpty());
    }

    @Test
    public void getMonedasRetornaListaNoVacia() {
        List<MonedaDTO> monedas = new ServicioAPIMercadoLibre().getMonedas();
        Assert.assertFalse(monedas.isEmpty());
    }
}
