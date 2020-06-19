package entidadOrganizativa;
import java.util.List;
import egreso.Egreso;
import proveedor.DireccionPostal;

public class OreganizacionSectorSocial extends EntidadJuridica {

    public OreganizacionSectorSocial(String razonSocial, String nombreFicticio, int cuit, DireccionPostal direccionPostal, List<Egreso> egreso) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal, egreso);
    }

    public OreganizacionSectorSocial(String razonSocial, String nombreFicticio, int cuit, DireccionPostal direccionPostal, Integer codigoIncripcionIGJ, List<Egreso> egresos) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal, codigoIncripcionIGJ, egresos);
    }
}
