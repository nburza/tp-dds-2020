package usuario;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
public class Administrador extends Usuario {

    public Administrador(String nombreUsuario, String contrasenia) throws ClassNotFoundException {
        super(nombreUsuario,contrasenia);
    }

    public Administrador() {
    }

    public boolean esAdmin(){
        return true;
    }
}
