import entidadOrganizativa.Entidad;
import entidadOrganizativa.EntidadBase;
import entidadOrganizativa.EntidadJuridica;
import org.passay.*;
import org.passay.dictionary.Dictionary;
import org.passay.dictionary.DictionaryBuilder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;

public class Usuario {

    String nombreUsuario;
    String contraseniaHasheada;
    List<Entidad> entidades = new ArrayList<>();


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
        String mensajeDeError = null;
        switch(ruleResultDetail.getErrorCode()) {
            case "ILLEGAL_WORD": mensajeDeError = "La contraseña ingresada es muy facil";
                break;
            case "TOO_SHORT": mensajeDeError = "La contraseña debe tener al menos 8 caracteres";
                break;
            case "TOO_LONG": mensajeDeError = "La contraseña puede tener 64 caracteres como maximo";
                break;
            case "ILLEGAL_USERNAME": mensajeDeError = "La contraseña debe ser distinta al nombre de usuario";
                break;
            case "ILLEGAL_ALPHABETICAL_SEQUENCE": mensajeDeError = "La contraseña no puede contener secuencias alfabeticas";
                break;
            case "ILLEGAL_NUMERICAL_SEQUENCE": mensajeDeError = "La contraseña no puede contener secuencias numericas";
                break;
            case "ILLEGAL_REPEATED_CHARS": mensajeDeError = "La contraseña contiene una repetición de caracteres";
                break;
        }
        return mensajeDeError;
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

    public void agregarEntidadJuridica(EntidadJuridica entidad ){
        this.entidades.add(entidad);
    }

    public void agregarEntidadBase(String nombreFicticio,String razonSocial,EntidadJuridica entidadJ) {
       EntidadBase entidadB= entidadJ.agregarEntidadBase(nombreFicticio,razonSocial);
        this.entidades.add(entidadB);
    }
}
