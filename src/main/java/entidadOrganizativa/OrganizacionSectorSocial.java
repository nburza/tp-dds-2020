package entidadOrganizativa;
import java.util.List;
import egreso.Egreso;
import presupuesto.DireccionPostal;

public class OrganizacionSectorSocial extends EntidadJuridica {

    public OrganizacionSectorSocial(String razonSocial, String nombreFicticio, int cuit, DireccionPostal direccionPostal, List<Egreso> egreso) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal, egreso);
    }

    public OrganizacionSectorSocial(String razonSocial, String nombreFicticio, int cuit, DireccionPostal direccionPostal, Integer codigoIncripcionIGJ, List<Egreso> egresos) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal, codigoIncripcionIGJ, egresos);
    }
}
