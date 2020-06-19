import egreso.DocComercial;
import egreso.Egreso;
import egreso.Item;
import egreso.TipoDocComercial;
import mediosDePago.Efectivo;
import mediosDePago.IdentificadorNullException;
import mediosDePago.MedioDePago;
import mediosDePago.TarjetaCredito;
import org.junit.Assert;
import org.junit.Test;
import proveedor.Moneda;
import presupuesto.Presupuesto;

import java.math.BigDecimal;
import java.util.ArrayList;

public class EgresoTest {
    @Test
    public void autenticoEgresoCorrecto() {
        Moneda moneda = new Moneda("ARS", "$", "pesos");
        Item item = new Item(null, new BigDecimal("100"), 1, moneda);
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(item);
        Egreso egreso = new Egreso(null, null, null, items, null, moneda);
        Assert.assertEquals(egreso.totalEgreso(), BigDecimal.valueOf(100));
    }

    @Test
    public void autenticoItemCorrecto() {
        Moneda moneda = new Moneda("ARS", "$", "pesos");
        Item item = new Item("lavandina", new BigDecimal("150"), 2, moneda);
        Assert.assertEquals(item.precioTotal(), BigDecimal.valueOf(300));
    }

    @Test
    public void autenticoDocComercialCorrecto() {
        DocComercial docComercial = new DocComercial(153460, TipoDocComercial.factura);
        Assert.assertEquals(docComercial.getTipoDC(), TipoDocComercial.factura);
    }

    @Test
    public void crearMedioDePagoCorrecto() {
        MedioDePago visa = new TarjetaCredito("visa", 1234567890987654L);
    }

    @Test(expected = IdentificadorNullException.class)
    public void elIdentificadorDelMedioPagoNoPuedeSerNull() {
        MedioDePago pagoFacil = new Efectivo("pagoFacil", null);
    }

    Egreso egreso1 = new Egreso(null, null, null, null,null,true,null);
    Presupuesto presu1 = new Presupuesto(new BigDecimal("100"),null,null,egreso1,null);
    Presupuesto presu2 = new Presupuesto(new BigDecimal("200"),null,null,egreso1,null);
    Presupuesto presu3 = new Presupuesto(new BigDecimal("300"),null,null,egreso1,null);
    @Test
    public void obtengoElMenorValorDePresupuesto() throws Exception {
        egreso1.agregarPresupuesto(presu1);
        egreso1.agregarPresupuesto(presu2);
        egreso1.agregarPresupuesto(presu3);
        Assert.assertEquals(egreso1.presupuestoMenorValor(), BigDecimal.valueOf(100));
    }
    @Test
    public void obtengoElMayorValorDePresupuesto() throws Exception {
        egreso1.agregarPresupuesto(presu1);
        egreso1.agregarPresupuesto(presu2);
        egreso1.agregarPresupuesto(presu3);
        Assert.assertEquals(egreso1.presupuestoMenorValor(), BigDecimal.valueOf(100));
    }
}
