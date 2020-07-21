package entidadOrganizativa;

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
}
