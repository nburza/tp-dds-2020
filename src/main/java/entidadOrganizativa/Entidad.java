package entidadOrganizativa;

import egreso.Egreso;
import egreso.Etiqueta;
import entidadOrganizativa.exceptions.MontoSuperadoException;
import persistencia.EntidadPersistente;

import javax.lang.model.element.Name;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Entidades")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "entidad", length = 7)
public abstract class Entidad extends EntidadPersistente {

    private String nombreFicticio;
    private String razonSocial;

    @ManyToMany
    private List <CategoriaEntidad> categoriaEntidad ;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "entidad_id")
    private List<Egreso> egresos;

    public Entidad(String nombreFicticio, String razonSocial, List<Egreso> egresos) {
        this.nombreFicticio = nombreFicticio;
        this.razonSocial = razonSocial;
        this.egresos = egresos;
        this.categoriaEntidad = new ArrayList<>();
    }

    public Entidad() {
    }

    public void setNombreFicticio(String nombreFicticio) {
        this.nombreFicticio = nombreFicticio;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public void setCategoriaEntidad(List<CategoriaEntidad> categoriaEntidad) {
        this.categoriaEntidad = categoriaEntidad;
    }

    public void setEgresos(List<Egreso> egresos) {
        this.egresos = egresos;
    }

    public String getNombreFicticio() {
        return nombreFicticio;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public abstract String getTipo();

    public boolean getEsJuridica() {
        return !this.getTipo().equals("Base");
    }

    public void agregarCategoria(CategoriaEntidad categoria) {
        this.categoriaEntidad.add(categoria);
    }

    public List<CategoriaEntidad> getCategoriaEntidad() {
        return categoriaEntidad;
    }

    public BigDecimal totalEgresos() {
        return egresos.stream()
                .map(Egreso::totalEgreso)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void agregarEgreso(Egreso egreso) {

        for (CategoriaEntidad categoria : categoriaEntidad) {
            categoria.verificarAgregadoDeEgreso(this, egreso);
        }
        egresos.add(egreso);

    }

    public List<Egreso> getEgresos(){
        return  egresos;
    }

    public Hashtable<Etiqueta,BigDecimal> reporteMensualGastosPorEtiqueta(){
        Hashtable<Etiqueta, BigDecimal> reporte = new Hashtable<>();
        etiquetasEnUso()
                .forEach(etiqueta -> reporte.put(etiqueta,gastosTotalPorEtiqueta(etiqueta, egresosDelUltimoMes())));
        return reporte;
    }

    public List<Etiqueta> etiquetasEnUso(){
        List<Etiqueta> etiquetas = new ArrayList<>();
        egresos.stream()
                .map(Egreso::getEtiquetas)
                .forEach(etiquetas::addAll);
        return etiquetas.stream()
                        .distinct()
                        .collect(Collectors.toList());
    }

    public List<Egreso> egresosDelUltimoMes(){
        return egresos.stream()
                .filter(Egreso::estaEnElUltimoMes)
                .collect(Collectors.toList());
    }

    public BigDecimal gastosTotalPorEtiqueta(Etiqueta etiqueta, List<Egreso> egresos){
        return egresos.stream()
                .filter(egreso -> egreso.tieneLaEtiqueta(etiqueta))
                .map(Egreso::totalEgreso)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean tieneCategoria(String nombreCategoria) {
        return categoriaEntidad.stream().anyMatch(c -> c.getNombre().equals(nombreCategoria));
    }

    public abstract List<Entidad> getEntidadesConSubentidades();
}
