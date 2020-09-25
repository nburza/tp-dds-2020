package entidadOrganizativa;

import egreso.Egreso;
import presupuesto.DireccionPostal;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", length = 1)
@DiscriminatorValue("EJ")
public abstract class EntidadJuridica extends Entidad {
    private int cuit;
    @OneToOne
    private DireccionPostal direccionPostal;
    private Integer codigoIncripcionIGJ;
    @OneToMany
    @JoinColumn(name = "entidad_juridica_id")
    private List<EntidadBase> listaEntidades = new ArrayList<>();

    public EntidadJuridica(String razonSocial, String nombreFicticio, int cuit, DireccionPostal direccionPostal, List<Egreso> egresos) {
        super(nombreFicticio, razonSocial, egresos);
        this.cuit = cuit;
        this.direccionPostal = direccionPostal;
        this.codigoIncripcionIGJ = null;
    }

    public EntidadJuridica(String razonSocial, String nombreFicticio, int cuit, DireccionPostal direccionPostal, Integer codigoIncripcionIGJ, List<Egreso> egresos) {
        super(nombreFicticio, razonSocial, egresos);
        this.cuit = cuit;
        this.direccionPostal = direccionPostal;
        this.codigoIncripcionIGJ = codigoIncripcionIGJ;
    }

    @Override
    public BigDecimal totalEgresos() {
        BigDecimal totalEntidadesBase = listaEntidades.stream()
                .map(Entidad::totalEgresos)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return super.totalEgresos().add(totalEntidadesBase);
    }

    public void agregarEntidadBase(EntidadBase entidadBase) {

        this.getCategoriaEntidad().forEach(CategoriaEntidad::verificarSiEntidadJuridicaPuedeAgregarEntidadesBase);

        entidadBase.getCategoriaEntidad().forEach(CategoriaEntidad::verificarSiEntidadBaseEsIncorporable);

        this.listaEntidades.add(entidadBase);
    }
}
