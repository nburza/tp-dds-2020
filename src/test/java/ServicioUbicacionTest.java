import org.junit.Assert;
import org.junit.Test;
import proveedor.*;

import java.util.List;
import java.util.Optional;

public class ServicioUbicacionTest {

    @Test
    public void getPaisesRetornaListaNoVacia() {
        List<Pais> paises = new ServicioUbicacionMercadoLibre().getPaises();
        Assert.assertFalse(paises.isEmpty());
        Pais primerPais = paises.get(0);
        Assert.assertEquals(primerPais.getName(),"Argentina");
    }

    @Test
    public void getProvinciasRetornaListaNoVacia() {
        Pais argentina = new Pais("AR","Argentina");
        List<Provincia> provincias = new ServicioUbicacionMercadoLibre().getProvincias(argentina);
        Assert.assertFalse(provincias.isEmpty());
    }

    @Test
    public void getCiudadesRetornaListaNoVacia() {
        Provincia catamarca = new Provincia("TUxBUENBVGFiY2Fm","Catamarca");
        List<Ciudad> ciudades = new ServicioUbicacionMercadoLibre().getCiudades(catamarca);
        Assert.assertFalse(ciudades.isEmpty());
    }

    @Test
    public void getMonedasRetornaListaNoVacia() {
        List<Moneda> monedas = new ServicioUbicacionMercadoLibre().getMonedas();
        Assert.assertFalse(monedas.isEmpty());
    }
}
