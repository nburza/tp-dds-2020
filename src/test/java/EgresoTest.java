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

import java.math.BigDecimal;
import java.util.ArrayList;

public class EgresoTest {
    @Test
    public void autenticoEgresoCorrecto() {
        Item item = new Item(null, new BigDecimal("100"), 1);
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(item);
        Egreso egreso = new Egreso(null, null, null, items, null);
        Assert.assertEquals(egreso.totalEgreso(), BigDecimal.valueOf(100));
    }

    @Test
    public void autenticoItemCorrecto() {
        Item item = new Item("lavandina", new BigDecimal("150"), 2);
        Assert.assertEquals(item.precioTotal(), BigDecimal.valueOf(300));
    }

    @Test
    public void autenticoDocComercialCorrecto() {
        DocComercial docComercial = new DocComercial(153460, TipoDocComercial.factura);
        Assert.assertEquals(docComercial.getTipoDC(), TipoDocComercial.factura);
    }

    @Test
    public void crearMedioDePagoCorrecto() {
        MedioDePago visa = new TarjetaCredito("visa", Long.valueOf("1234567890987654"));
    }

    @Test(expected = IdentificadorNullException.class)
    public void elIdentificadorDelMedioPagoNoPuedeSerNull() {
        MedioDePago pagoFacil = new Efectivo("pagoFacil", null);
    }
}
