package entidadOrganizativa;
import egreso.Egreso;
import presupuesto.DireccionPostal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Entity
@DiscriminatorValue("EMP")
public class Empresa extends EntidadJuridica {

    @Enumerated(EnumType.STRING)
    private CategoriaEmpresa categoria;

    public Empresa(String razonSocial, String nombreFicticio, int cuit, DireccionPostal direccionPostal, CategoriaEmpresa categoria, List<Egreso> egresos) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal, egresos);
        this.categoria=categoria;
    }

    public Empresa(String razonSocial, String nombreFicticio, int cuit, DireccionPostal direccionPostal, Integer codigoIncripcionIGJ, CategoriaEmpresa categoria, List<Egreso> egresos) {
        super(razonSocial, nombreFicticio, cuit, direccionPostal, codigoIncripcionIGJ, egresos);
        this.categoria=categoria;
    }

    public Empresa() {
    }

    @Override
    public String getTipo() {
        return "Empresa";
    }

    public void setCategoria(CategoriaEmpresa categoria) {
        this.categoria = categoria;
    }
}
