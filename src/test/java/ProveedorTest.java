import org.junit.Assert;
import org.junit.Test;
import proveedor.DireccionPostal;
import proveedor.Proveedor;

public class ProveedorTest {

    @Test
    public void crearProveedor() {

        Proveedor proveedor = new Proveedor("Carlitos SRL", "20-40131392-4", null);
        Assert.assertNotNull(proveedor);
    }

    @Test
    public void crearProveedorConDireccionPostal() {
        Proveedor proveedor = new Proveedor(null,null,new DireccionPostal("Argentina","Chubut","Biedma",null));
        Assert.assertEquals("Biedma", proveedor.getDireccionPostal().getCiudad());
    }
}
