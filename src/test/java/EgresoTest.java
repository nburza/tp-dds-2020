import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;

public class EgresoTest
{
    @Test
    public void AutenticoEgresoCorrecto()
    {
        Item item = new Item(null,100,1);
        ArrayList<Item> items= new ArrayList<Item>();
        items.add(item);
        Egreso egreso = new Egreso(null,null,null, items, null);
        Assert.assertEquals(egreso.totalEgreso(), (float)100, 0.0001);
    }

    @Test
    public void AutenticoItemCorrecto()
    {
        Item item = new Item("lavandina", 150, 2);
        Assert.assertEquals(item.precioTotal(), (float)300, 0.0001);
    }

    @Test
    public void AutenticoDocComercialCorrecto()
    {
        DocComercial docComercial = new DocComercial(153460, TipoDocComercial.factura);
        Assert.assertEquals(docComercial.tipoDC, TipoDocComercial.factura);
    }
}
