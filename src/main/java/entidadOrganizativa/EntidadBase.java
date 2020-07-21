package entidadOrganizativa;

import java.util.ArrayList;
import java.util.List;
import egreso.Egreso;

public class EntidadBase extends Entidad {

    public EntidadBase(String nombreFicticio, String razonSocial, List<Egreso> egresos) {
        super(nombreFicticio, razonSocial, egresos);
    }
}
