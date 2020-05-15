import mediosDePago.Efectivo;
import mediosDePago.IdentificadorNullException;
import mediosDePago.MedioDePago;
import mediosDePago.TarjetaCredito;
import org.junit.Test;

public class EgresosTests {
    @Test
    public void crearTarjetaCreditoCorrecta() {
        MedioDePago visa = new TarjetaCredito("visa", Long.valueOf("1234567890987654"));
    }

    @Test (expected = IdentificadorNullException.class)
    public void elIdentificadorDelMedioPagoNoPuedeSerNull(){
        MedioDePago pagoFacil = new Efectivo("pagoFacil",null);
    }
}
