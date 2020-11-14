package entidadOrganizativa;

import egreso.Egreso;
import presupuesto.DireccionPostal;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public abstract class EntidadJuridica extends Entidad {
    private int cuit;
    @Embedded
    private DireccionPostal direccionPostal;
    private Integer codigoIncripcionIGJ;
    @OneToMany(cascade = CascadeType.ALL)
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

    public EntidadJuridica() {
    }

    public void setCuit(int cuit) {
        this.cuit = cuit;
    }

    public void setDireccionPostal(DireccionPostal direccionPostal) {
        this.direccionPostal = direccionPostal;
    }

    public void setCodigoIncripcionIGJ(Integer codigoIncripcionIGJ) {
        this.codigoIncripcionIGJ = codigoIncripcionIGJ;
    }

    public void setListaEntidades(List<EntidadBase> listaEntidades) {
        this.listaEntidades = listaEntidades;
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

    public String getCantidadDeEntidadesAsignadasComoString() {
        return String.valueOf(this.listaEntidades.size());
    }

    @Override
    public List<Entidad> getEntidadesConSubentidades() {
        List<Entidad> entidadConSubentidades = new ArrayList<>();
        entidadConSubentidades.add(this);
        this.listaEntidades.forEach(e -> entidadConSubentidades.add(e));
        return entidadConSubentidades;
    }
}
