package entidadOrganizativa;

import egreso.Egreso;
import entidadOrganizativa.exceptions.EntidadBaseNoIncorporableException;
import entidadOrganizativa.exceptions.EntidadSinEntidadesBaseException;
import entidadOrganizativa.exceptions.MontoSuperadoException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CategoriaEntidad {

    private String nombre;
    private BigDecimal montoLimite;
    private List<Regla> reglas;

    public CategoriaEntidad(String nombre, BigDecimal montoLimite, List<Regla> reglas) {
        this.nombre = nombre;
        this.montoLimite = montoLimite;
        this.reglas = reglas;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getMontoLimite() {
        return montoLimite;
    }

    public void setMontoLimite(BigDecimal montoLimite) {
        this.montoLimite = montoLimite;
    }

    public List<Regla> getReglas() {
        return reglas;
    }

    public void agregarRegla(Regla regla) {
        this.reglas.add(regla);
    }

    public void removerRegla(Regla regla) {
        this.reglas.remove(regla);
    }

    private boolean superaMontoLimite(Entidad entidad, Egreso egreso) {
        BigDecimal montoFuturo = entidad.totalEgresos().add(egreso.totalEgreso());
        return montoFuturo.compareTo(montoLimite) > 0;
    }

    public void verificarAgregadoDeEgreso(Entidad entidad, Egreso egreso) {
        if (this.reglas.contains(Regla.BLOQUEO_EGRESOS_POR_MONTO) &&
                superaMontoLimite(entidad, egreso)) {
            throw new MontoSuperadoException("No se puede agregar el egreso, se superó el monto limite");
        }
    }

    public void verificarSiEntidadBaseEsIncorporable() {
        if(this.reglas.contains(Regla.ENTIDAD_BASE_NO_INCORPORABLE)) {
            throw new EntidadBaseNoIncorporableException("No se puede incorporar esta entidad base");
        }
    }

    public void verificarSiEntidadJuridicaPuedeAgregarEntidadesBase() {
        if(this.reglas.contains(Regla.ENTIDAD_JURIDICA_SIN_ENTIDADES_BASE)) {
            throw new EntidadSinEntidadesBaseException("Esta entidad juridica no puede incorporar entidades base");
        }
    }


}
