import egreso.Egreso;
import entidadOrganizativa.Entidad;
import entidadOrganizativa.EntidadBase;
import entidadOrganizativa.EntidadJuridica;
import org.passay.*;
import org.passay.dictionary.Dictionary;
import org.passay.dictionary.DictionaryBuilder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Usuario {

    String nombreUsuario;
    String contraseniaHasheada;

    public Usuario(String nombreUsuario, String contrasenia) {
        validarContrasenia(nombreUsuario,contrasenia);
        this.nombreUsuario = nombreUsuario;
        this.contraseniaHasheada = hashearContrasenia(contrasenia);
    }
    
    private void validarContrasenia(String nombreUsuario, String contrasenia) {
        PasswordData passwordData = new PasswordData();
        passwordData.setUsername(nombreUsuario);
        passwordData.setPassword(contrasenia);

        PasswordValidator passwordValidator = getPasswordValidator();

        RuleResult validate = passwordValidator.validate(passwordData);
        if(!validate.isValid()) {
            throw new ContraseniaDebilException(definirMensajeDelError(validate));
        }
    }

    private String definirMensajeDelError(RuleResult validate) {
        RuleResultDetail ruleResultDetail = validate.getDetails().get(0);
        return String.valueOf(getDiccionarioDeErrores().get(ruleResultDetail.getErrorCode()));
    }

    private Hashtable<String, String> getDiccionarioDeErrores() {
        Hashtable<String, String> DiccionarioDeErrores = new Hashtable<>();
        DiccionarioDeErrores.put("ILLEGAL_WORD","La contraseña ingresada es muy facil");
        DiccionarioDeErrores.put("TOO_SHORT","La contraseña debe tener al menos 8 caracteres");
        DiccionarioDeErrores.put("TOO_LONG","La contraseña puede tener 64 caracteres como maximo");
        DiccionarioDeErrores.put("ILLEGAL_USERNAME","La contraseña debe ser distinta al nombre de usuario");
        DiccionarioDeErrores.put("ILLEGAL_ALPHABETICAL_SEQUENCE","La contraseña no puede contener secuencias alfabeticas");
        DiccionarioDeErrores.put("ILLEGAL_NUMERICAL_SEQUENCE","La contraseña no puede contener secuencias numericas");
        DiccionarioDeErrores.put("ILLEGAL_REPEATED_CHARS","La contraseña contiene una repetición de caracteres");
        return DiccionarioDeErrores;
    }

    private PasswordValidator getPasswordValidator() {
        return new PasswordValidator(
                    reglaConClavesBaneadas(),
                    new LengthRule(8,64),
                    new UsernameRule(),
                    new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
                    new IllegalSequenceRule(EnglishSequenceData.Numerical),
                    new RepeatCharactersRule()
            );
    }

    private DictionaryRule reglaConClavesBaneadas() {
        DictionaryBuilder dictionaryBuilder = new DictionaryBuilder();
        Dictionary diccionarioContraseniasFaciles = dictionaryBuilder.addFile("src/main/resources/10k-most-common.txt").build();
        return new DictionaryRule(diccionarioContraseniasFaciles);
    }

    private String hashearContrasenia(String contraseniaPlana) {
        return BCrypt.hashpw(contraseniaPlana, BCrypt.gensalt());
    }

    private boolean contraseniaEsCorrecta(String contraseniaCandidata) {
        return BCrypt.checkpw(contraseniaCandidata, this.contraseniaHasheada);
    }

    public boolean autenticar(String nombreUsuario, String contrasenia) {
        return this.nombreUsuario.equals(nombreUsuario) &&
                contraseniaEsCorrecta(contrasenia);
    }
}
