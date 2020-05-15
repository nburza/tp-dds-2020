package entidadOrganizativa;

public class EntidadBase implements Entidad {
    private String nombreFicticio;
    private String razonSocial;

    public EntidadBase(String nombreFicticio, String razonSocial) {
        this.nombreFicticio = nombreFicticio;
        this.razonSocial = razonSocial;
    }


}
