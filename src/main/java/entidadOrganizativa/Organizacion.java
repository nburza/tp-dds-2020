package entidadOrganizativa;

import java.util.List;

public class Organizacion {

    private List<Entidad> entidades;
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
