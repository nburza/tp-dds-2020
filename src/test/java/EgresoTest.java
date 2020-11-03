import egreso.*;
import mediosDePago.Efectivo;
import mediosDePago.IdentificadorNullException;
import mediosDePago.MedioDePago;
import mediosDePago.TarjetaCredito;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import presupuesto.Presupuesto;
import apiMercadoLibre.DTO.MonedaDTO;
import presupuesto.Proveedor;
import apiMercadoLibre.ServiceLocator;
import apiMercadoLibre.ValidadorDeMoneda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EgresoTest {

    private Proveedor proveedor;
    private Egreso egreso1;
    private Producto producto1;
    private Producto producto2;
    private Producto producto3;
    private Item item1;
    private Item item2;
    private Item item3;
    private Presupuesto presu1;
    private Presupuesto presu2;
    private Presupuesto presu3;

    @Before
    public void objetosTest() throws Exception {
        MonedaDTO pesoArgentino = new MonedaDTO(null,null,"Peso argentino");
        ValidadorDeMoneda validadorDeMoneda = new ValidadorDeMoneda(Arrays.asList(pesoArgentino));
        ServiceLocator.getInstance().setValidadorDeMoneda(validadorDeMoneda);
        proveedor = new Proveedor(null,null,null);
        egreso1 = new Egreso(null, null, new ArrayList<>(),null,"Peso argentino");
        producto1 = new Producto(null);
        producto2 = new Producto(null);
        producto3 = new Producto(null);
        item1 = new Item(producto1, 1, "Peso argentino", new BigDecimal("100"));
        item2 = new Item(producto2, 1, "Peso argentino", new BigDecimal("200"));
        item3 = new Item(producto3, 1, "Peso argentino", new BigDecimal("300"));
        presu1 = new Presupuesto(Arrays.asList(item1),null, egreso1, "Peso argentino", proveedor);
        presu2 = new Presupuesto(Arrays.asList(item2),null, egreso1, "Peso argentino", proveedor);
        presu3 = new Presupuesto(Arrays.asList(item3),null, egreso1, "Peso argentino", proveedor);
    }

    @Test
    public void autenticoEgresoCorrecto() {
        Producto producto = new Producto(null);
        Item item = new Item(producto,1, "Peso argentino", new BigDecimal("100"));
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(item);
        Egreso egreso = new Egreso(null, null, items, null, "Peso argentino");
        Assert.assertEquals(egreso.totalEgreso(), BigDecimal.valueOf(100));
    }

    @Test
    public void autenticoItemCorrecto() {
        Producto producto = new Producto("lavandina");
        Item item = new Item(producto, 2, "Peso argentino", new BigDecimal("150"));
        Assert.assertEquals(item.precioTotal(), BigDecimal.valueOf(300));
    }

    @Test
    public void autenticoDocComercialCorrecto() {
        DocComercial docComercial = new DocComercial(153460, TipoDocComercial.factura);
        Assert.assertEquals(docComercial.getTipoDC(), TipoDocComercial.factura);
    }

    @Test
    public void creoEgresoConEtiquetas(){
        Etiqueta etiqueta = new Etiqueta("Amoblamiento");
        List<Etiqueta>etiquetas = new ArrayList<>();
        etiquetas.add(etiqueta);
        Egreso egreso = new Egreso(null,null,null,null,"Peso argentino");
        egreso.agregarEtiqueta(etiqueta);
        Assert.assertTrue(egreso.tieneLaEtiqueta(etiqueta));
    }

    @Test
    public void crearMedioDePagoCorrecto() {
        MedioDePago visa = new TarjetaCredito("visa", "1234567890987654L");
    }

    @Test(expected = IdentificadorNullException.class)
    public void elIdentificadorDelMedioPagoNoPuedeSerNull() {
        MedioDePago pagoFacil = new Efectivo("pagoFacil", null);
    }

    @Test
    public void obtengoElMenorValorDePresupuesto() throws Exception {
        Assert.assertEquals(egreso1.cumpleCriterioSeleccion(),true);
    }

    @Test(expected = NullPointerException.class)
    public void noSePuedeCrearUnPresupuestoSinEgreso() throws Exception {
        Presupuesto presu = new Presupuesto(null,null,null,"Peso argentino", null);
    }

    @Test
    public void cantPresupuestosMenorAlRequerido() throws Exception{
        Assert.assertFalse(egreso1.esValido());
    }

    @Test
    public void presupuestoMalElegido() throws Exception{
        Producto producto = new Producto(null);
        egreso1.getItems().add(new Item(producto, 1, "Peso argentino", new BigDecimal("200")));
        Assert.assertFalse(egreso1.esValido());
    }

    @Test
    public void itemsDistintosAlDetalle() throws Exception{
        Producto producto = new Producto(null);
        egreso1.getItems().add(new Item(producto, 1, "Peso argentino", new BigDecimal("100")));
        Assert.assertFalse(egreso1.esValido());
    }

    @Test
    public void calculoTotalDeUnEgreso() {
        egreso1.getItems().add(item1);
        egreso1.getItems().add(item2);
        egreso1.getItems().add(item3);
        Assert.assertEquals(BigDecimal.valueOf(600), egreso1.totalEgreso());
    }
}