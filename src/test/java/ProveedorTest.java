import org.junit.Assert;
import org.junit.Test;
import proveedor.Proveedor;

public class ProveedorTest {

    @Test
    public void crearProveedor() {

        Proveedor proveedor = new Proveedor("Carlitos SRL", "20-40131392-4", null, true);
        Assert.assertNotNull(proveedor);
    }
}
