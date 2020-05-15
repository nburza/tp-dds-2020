package entidadOrganizativa;

import java.util.ArrayList;
import java.util.List;

public abstract class EntidadJuridica implements Entidad {
    String razonSocial;
    String nombreFicticio;
    int cuit;
    int direccionPostal;
    Integer codigoIncripcionIGJ;

    private List<EntidadBase> listaEntidades = new ArrayList<>();

    public EntidadJuridica(String razonSocial, String nombreFicticio, int cuit, int direccionPostal) {
        this.razonSocial = razonSocial;
        this.nombreFicticio = nombreFicticio;
        this.cuit = cuit;
        this.direccionPostal = direccionPostal;
        this.codigoIncripcionIGJ = null;
    }

    public EntidadJuridica(String razonSocial, String nombreFicticio, int cuit, int direccionPostal, Integer codigoIncripcionIGJ) {
        this.razonSocial = razonSocial;
        this.nombreFicticio = nombreFicticio;
        this.cuit = cuit;
        this.direccionPostal = direccionPostal;
        this.codigoIncripcionIGJ = codigoIncripcionIGJ;
    }


    public EntidadBase agregarEntidadBase(String nombreFicticio, String razonSocial) {
       //Lo ideal es que sea esta la unica forma de crear entidades base
        EntidadBase entidad = new EntidadBase(nombreFicticio, razonSocial);
        this.listaEntidades.add(entidad);
        return entidad;
    }

}
