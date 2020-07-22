package entidadOrganizativa;

import egreso.Egreso;
import egreso.Etiqueta;
import entidadOrganizativa.exceptions.MontoSuperadoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Entidad  {

    private String nombreFicticio;
    private String razonSocial;
    private CategoriaEntidad categoriaEntidad;
    private List<Egreso> egresos;

    public Entidad(String nombreFicticio, String razonSocial, List<Egreso> egresos) {
        this.nombreFicticio = nombreFicticio;
        this.razonSocial = razonSocial;
        this.egresos = egresos;
    }

    public void configurarCategoria(CategoriaEntidad categoria) {
        this.categoriaEntidad = categoria;
    }

    public CategoriaEntidad getCategoriaEntidad() {
        return categoriaEntidad;
    }

    public BigDecimal totalEgresos() {
        return egresos.stream()
                .map(Egreso::totalEgreso)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean superoMontoLimite(Egreso egreso) {
        BigDecimal montoLimite = categoriaEntidad.getMontoLimite();
        BigDecimal montoFuturo = totalEgresos().add(egreso.totalEgreso());
        return montoFuturo.compareTo(montoLimite) > 0;
    }

    public void agregarEgreso(Egreso egreso) {
        if(this.categoriaEntidad != null) {
            this.categoriaEntidad.verificarAgregadoDeEgreso(this, egreso);
        }
        egresos.add(egreso);
    }

    public BigDecimal gastosTotalPorEtiqueta(Etiqueta etiqueta){
        return egresos.stream()
                .filter(egreso -> egreso.getEtiquetas().contains(etiqueta))
                .map(Egreso::totalEgreso)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
