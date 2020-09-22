package entidadOrganizativa;

import persistencia.EntidadPersistente;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.List;

@Entity
public class Organizacion extends EntidadPersistente {

    //@OneToMany
    //@JoinColumn(name = "organizacion_id")
    @Transient
    private List<Entidad> entidades;
    @OneToMany
    @JoinColumn(name = "organizacion_id")
    private List<CategoriaEntidad> categorias;

    public Organizacion(List<Entidad> entidades, List<CategoriaEntidad> categorias) {
        this.entidades = entidades;
        this.categorias = categorias;
    }

    public void agregarEntidad(Entidad entidad) {
        this.entidades.add(entidad);
    }

    public void agregarCategoria(CategoriaEntidad categoria) {
        this.categorias.add(categoria);
    }

    public void eliminarCategoria(CategoriaEntidad categoria) {
        this.categorias.remove(categoria);
    }

    public void agregarCategoriaAEntidad(Entidad entidad, CategoriaEntidad categoria) {
        entidad.agregarCategoria(categoria);
    }
}
