package entidadOrganizativa;

public class Osc extends EntidadJuridica {

    public Osc(String razonSocial, String nombreFicticio, int cuit, int direccionPostal) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal);
    }

    public Osc(String razonSocial, String nombreFicticio, int cuit, int direccionPostal, Integer codigoIncripcionIGJ) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal, codigoIncripcionIGJ);
    }
}
