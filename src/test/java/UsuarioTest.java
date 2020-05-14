import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UsuarioTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void AutenticoUsuarioCorrecto() {

        Usuario usuario = new Usuario("nombre","queremos la copa");
        Assert.assertTrue(usuario.autenticar("nombre","queremos la copa"));
    }

    @Test
    public void AutenticoAdminIncorrecto() {

        Administrador admin = new Administrador("nombre","queremos la copa");
        Assert.assertFalse(admin.autenticar("nombre","queremos la copita"));
    }

    @Test
    public void ContraseniaFacilArrojaExcepcion() {
        exceptionRule.expect(ContraseniaDebilException.class);
        exceptionRule.expectMessage("La contraseña ingresada es muy facil");
        new Usuario("nombre","football");
    }

    @Test
    public void ContraseniaCortaArrojaExcepcion() {
        exceptionRule.expect(ContraseniaDebilException.class);
        exceptionRule.expectMessage("La contraseña debe tener al menos 8 caracteres");
        new Usuario("nombre", "clave");
    }

    @Test
    public void ContraseniaLargaArrojaExcepcion() {
        exceptionRule.expect(ContraseniaDebilException.class);
        exceptionRule.expectMessage("La contraseña puede tener 64 caracteres como maximo");
        new Usuario("nombre", "Esta clave es muy larga por el hecho de que " +
                "tengo que llegar a mas de 64 caracteres, no se a que loco se le ocurriria poner " +
                "una contraseña tan larga, la verdad que le llegas a errar a una letra y te la " +
                "regalo");
    }

    @Test
    public void ContraseniaConNombreDeUsuarioArrojaExcepcion() {
        exceptionRule.expect(ContraseniaDebilException.class);
        exceptionRule.expectMessage("La contraseña debe ser distinta al nombre de usuario");
        new Usuario("nombre de usuario", "nombre de usuario");
    }

    @Test
    public void ContraseniaConSecuenciaAlfabeticaArrojaExcepcion() {
        exceptionRule.expect(ContraseniaDebilException.class);
        exceptionRule.expectMessage("La contraseña no puede contener secuencias alfabeticas");
        new Usuario("nombre", "abcdefghijk");
    }

    @Test
    public void ContraseniaConSecuenciaNumericaArrojaExcepcion() {
        exceptionRule.expect(ContraseniaDebilException.class);
        exceptionRule.expectMessage("La contraseña no puede contener secuencias numericas");
        new Usuario("nombre", "hola12345");
    }

    @Test
    public void ContraseniaConRepeticionDeCaracteresArrojaExcepcion() {
        exceptionRule.expect(ContraseniaDebilException.class);
        exceptionRule.expectMessage("La contraseña contiene una repetición de caracteres");
        new Usuario("nombre", "holaaaaaaa");
    }
}
