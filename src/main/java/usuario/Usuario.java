package usuario;

import egreso.Egreso;
import org.passay.*;
import org.passay.dictionary.Dictionary;
import org.passay.dictionary.DictionaryBuilder;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import egreso.ValidadorDeEgresos;
import persistencia.EntidadPersistente;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "rol", length = 1)
@DiscriminatorValue("U")
public class Usuario extends EntidadPersistente {

    private String nombreUsuario;
    private String contraseniaHasheada;

    public Usuario() {
    }

    public Usuario(String nombreUsuario, String contrasenia) throws ClassNotFoundException {
        validarContrasenia(nombreUsuario,contrasenia);
        this.nombreUsuario = nombreUsuario;
        this.contraseniaHasheada = hashearContrasenia(contrasenia);
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContraseniaHasheada(String contraseniaHasheada) {
        this.contraseniaHasheada = contraseniaHasheada;
    }

    public Hashtable<Egreso, String> consultarBandeja()
    {
        return ValidadorDeEgresos.getInstance().getAll(this);
    }
    
    private void validarContrasenia(String nombreUsuario, String contrasenia) throws ClassNotFoundException {
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

    private PasswordValidator getPasswordValidator() throws ClassNotFoundException {
        return new PasswordValidator(
                    reglaConClavesBaneadas(),
                    new LengthRule(8,64),
                    new UsernameRule(),
                    new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
                    new IllegalSequenceRule(EnglishSequenceData.Numerical),
                    new RepeatCharactersRule()
            );
    }

    private DictionaryRule reglaConClavesBaneadas() throws ClassNotFoundException {
        Class cls = Class.forName("usuario.Usuario");
        ClassLoader cLoader = cls.getClassLoader();
        InputStream inputStream = cLoader.getResourceAsStream("10k-most-common.txt");
        DictionaryBuilder dictionaryBuilder = new DictionaryBuilder();
        Dictionary diccionarioContraseniasFaciles = dictionaryBuilder.addReader(new InputStreamReader(inputStream)).build();
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

    public boolean esAdmin(){
        return false;
    }
}