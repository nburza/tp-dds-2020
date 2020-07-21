package entidadOrganizativa;

import egreso.Egreso;
import entidadOrganizativa.exceptions.MontoSuperadoException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        if(categoriaEntidad != null) {
            if(categoriaEntidad.getReglas().contains(Regla.BLOQUEO_EGRESOS_POR_MONTO) &&
                    superoMontoLimite(egreso)){
                throw new MontoSuperadoException("No se puede agregar el egreso, se superó el monto limite");
            }
        }
        egresos.add(egreso);
    }
}
