package entidadOrganizativa;

import java.util.ArrayList;
import java.util.List;
import egreso.Egreso;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("EB")
public class EntidadBase extends Entidad {

    public EntidadBase(String nombreFicticio, String razonSocial, List<Egreso> egresos) {
        super(nombreFicticio, razonSocial, egresos);
    }

    public EntidadBase() {
    }

    @Override
    public String getTipo() {
        return "Base";
    }
}
