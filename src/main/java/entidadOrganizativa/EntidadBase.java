package entidadOrganizativa;

import java.util.ArrayList;
import java.util.List;
import egreso.Egreso;

public class EntidadBase implements Entidad {
    private String nombreFicticio;
    private String razonSocial;
    private List<Egreso> egresos;

    public EntidadBase(String nombreFicticio, String razonSocial, List<Egreso> egresos) {
        this.nombreFicticio = nombreFicticio;
        this.razonSocial = razonSocial;
        this.egresos = egresos;
    }


}
