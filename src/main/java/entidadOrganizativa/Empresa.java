package entidadOrganizativa;
import egreso.Egreso;
import java.util.List;

public class Empresa extends EntidadJuridica {

    CategoriaEmpresa categoria;

    public Empresa(String razonSocial, String nombreFicticio, int cuit, int direccionPostal,CategoriaEmpresa categoria, List<Egreso> egresos) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal, egresos);
        this.categoria=categoria;
    }

    public Empresa(String razonSocial, String nombreFicticio, int cuit, int direccionPostal, Integer codigoIncripcionIGJ, CategoriaEmpresa categoria, List<Egreso> egresos) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal, codigoIncripcionIGJ, egresos);
        this.categoria=categoria;
    }

}
