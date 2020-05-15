import org.junit.Assert;
import org.junit.Test;

public class ProveedorTest {

    @Test
    public void CrearProveedor() {

        Proveedor proveedor = new Proveedor("Carlitos SRL", "20-40131392-4", "Guamini 4856");
        Assert.assertNotNull(proveedor);
    }
}
