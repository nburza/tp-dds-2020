package entidadOrganizativa;

import egreso.Egreso;
import persistencia.EntidadPersistente;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CategoriaEntidad extends EntidadPersistente {

    private String nombre;
    @ManyToMany(cascade= CascadeType.ALL)
    @OrderColumn(name = "orden")
    private List<Regla> reglas;

    public CategoriaEntidad(String nombre, List<Regla> reglas) {
        this.nombre = nombre;
        this.reglas = reglas;
    }

    public CategoriaEntidad(String nombre) {
        this.nombre = nombre;
        this.reglas = new ArrayList<>();
    }

    public CategoriaEntidad() {
    }

    public void setReglas(List<Regla> reglas) {
        this.reglas = reglas;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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



    public void verificarAgregadoDeEgreso(Entidad entidad, Egreso egreso) {

        reglas.forEach(unaRegla -> unaRegla.verificarAgregadoDeEgreso(entidad, egreso));

    }

    public void verificarSiEntidadBaseEsIncorporable() {

        reglas.forEach(Regla::verificarSiEntidadBaseEsIncorporable);

    }

    public void verificarSiEntidadJuridicaPuedeAgregarEntidadesBase() {

        reglas.forEach(Regla::verificarSiEntidadJuridicaPuedeAgregarEntidadesBase);
    }


}
