package entidadOrganizativa;
import egreso.Egreso;
import java.util.ArrayList;
import java.util.List;


public abstract class EntidadJuridica implements Entidad {
    String razonSocial;
    String nombreFicticio;
    int cuit;
    String direccionPostal;
    Integer codigoIncripcionIGJ;
    List<Egreso> egresos;

    private List<EntidadBase> listaEntidades = new ArrayList<>();

    public EntidadJuridica(String razonSocial, String nombreFicticio, int cuit, String direccionPostal, List<Egreso> egresos) {
        this.razonSocial = razonSocial;
        this.nombreFicticio = nombreFicticio;
        this.cuit = cuit;
        this.direccionPostal = direccionPostal;
        this.codigoIncripcionIGJ = null;
        this.egresos = egresos;
    }

    public EntidadJuridica(String razonSocial, String nombreFicticio, int cuit, String direccionPostal, Integer codigoIncripcionIGJ, List<Egreso> egresos) {
        this.razonSocial = razonSocial;
        this.nombreFicticio = nombreFicticio;
        this.cuit = cuit;
        this.direccionPostal = direccionPostal;
        this.codigoIncripcionIGJ = codigoIncripcionIGJ;
        this.egresos = egresos;
    }


    public EntidadBase agregarEntidadBase(String nombreFicticio, String razonSocial, List<Egreso> egresos) {
       //Lo ideal es que sea esta la unica forma de crear entidades base
        EntidadBase entidad = new EntidadBase(nombreFicticio, razonSocial, egresos);
        this.listaEntidades.add(entidad);
        return entidad;
    }

}
