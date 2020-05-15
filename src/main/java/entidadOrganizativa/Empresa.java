package entidadOrganizativa;

public class Empresa extends EntidadJuridica {

    CategoriaEmpresa categoria;

    public Empresa(String razonSocial, String nombreFicticio, int cuit, int direccionPostal,CategoriaEmpresa categoria ) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal);
        this.categoria=categoria;
    }

    public Empresa(String razonSocial, String nombreFicticio, int cuit, int direccionPostal, Integer codigoIncripcionIGJ, CategoriaEmpresa categoria) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal, codigoIncripcionIGJ);
        this.categoria=categoria;
    }

}
