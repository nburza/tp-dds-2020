package entidadOrganizativa;
import java.util.List;
import egreso.Egreso;

public class Osc extends EntidadJuridica {

    public Osc(String razonSocial, String nombreFicticio, int cuit, String direccionPostal, List<Egreso> egreso) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal, egreso);
    }

    public Osc(String razonSocial, String nombreFicticio, int cuit, String direccionPostal, Integer codigoIncripcionIGJ, List<Egreso> egresos) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal, codigoIncripcionIGJ, egresos);
    }
}