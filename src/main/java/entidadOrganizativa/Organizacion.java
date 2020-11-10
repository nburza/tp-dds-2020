package entidadOrganizativa;

import persistencia.EntidadPersistente;
import usuario.Usuario;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Organizacion extends EntidadPersistente {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizacion_id")
    private List<Entidad> entidades;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizacion_id")
    private List<CategoriaEntidad> categorias;
    @OneToMany
    private List<Usuario> usuarios = new ArrayList<>();

    public Organizacion(List<Entidad> entidades, List<CategoriaEntidad> categorias) {
        this.entidades = entidades;
        this.categorias = categorias;
    }

    public Organizacion() {
    }

    public void setEntidades(List<Entidad> entidades) {
        this.entidades = entidades;
    }

    public void setCategorias(List<CategoriaEntidad> categorias) {
        this.categorias = categorias;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Entidad> getEntidades() {
        return entidades;
    }

    public List<CategoriaEntidad> getCategorias() {
        return categorias;
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

    public boolean contieneAlUsuarioConId(Long idUsuario) {
        return usuarios.stream().anyMatch(u -> u.getId().equals(idUsuario));
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
}
