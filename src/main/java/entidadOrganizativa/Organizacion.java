package entidadOrganizativa;

import persistencia.EntidadPersistente;
import usuario.Usuario;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Organizacion extends EntidadPersistente {

    private String nombre;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizacion_id")
    private List<Entidad> entidades;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizacion_id")
    private List<CategoriaEntidad> categorias;
    @OneToMany
    private List<Usuario> usuarios = new ArrayList<>();

    public Organizacion(String nombre, List<Entidad> entidades, List<CategoriaEntidad> categorias) {
        this.nombre = nombre;
        this.entidades = entidades;
        this.categorias = categorias;
    }

    public Organizacion() {
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Entidad getEntidadPorId(long id){
        return entidades.stream().filter(entidad1->entidad1.getId().equals(id)).findFirst().get();
    }

    public String getNombre(){
        return this.nombre;
    }

    public List<CategoriaEntidad> getCategorias() {
        return categorias;
    }

    public CategoriaEntidad getCategoriaPorId(Long id){
        return this.categorias.stream().filter(c -> c.getId().equals(id)).findFirst().get();
    }

    public List<CategoriaEntidad> getCategoriasPorListaDeIds(List<Long> ids) {
        return this.categorias.stream().filter(c -> ids.stream().anyMatch(i -> i.equals(c.getId()))).collect(Collectors.toList());
    }

    public void agregarEntidad(Entidad entidad) {
        this.entidades.add(entidad);
    }

    public void removerEntidad(Entidad entidad) {
        Entidad entidadConId = this.entidades.stream().filter(e -> e.getId().equals(entidad.getId())).findFirst().get();
        this.entidades.remove(entidadConId);
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

    public List<Entidad> getEntidadesPorCategoria(Long idCategoria) {
        return entidades.stream().filter(e -> e.tieneCategoria(idCategoria)).collect(Collectors.toList());
    }

    public List<Entidad> getEntidadesBaseAsignables() {
        return this.entidades.stream().filter(e -> e.esAsignable()).collect(Collectors.toList());
    }

    public void asignarEntidadesBaseAUnaJuridica(List<Entidad> entidadesBase, EntidadJuridica entidadJuridica) {
        entidadesBase.forEach(eb -> {
            this.removerEntidad(eb);
            entidadJuridica.agregarEntidadBase((EntidadBase) eb);
        });
    }

    public List<Entidad> getEntidadesConSubentidades() {
        List<Entidad> todasLasEntidades = new ArrayList<>();
        this.entidades.forEach(e -> e.getEntidadesConSubentidades().forEach(ent -> todasLasEntidades.add(ent)));
        return todasLasEntidades;
    }

    public EntidadJuridica getEntidadJuridica(Long id) {
        return (EntidadJuridica) getEntidades().stream().filter(e -> e.getId().equals(id)).findFirst().get();
    }

    public List<Entidad> getEntidadesBaseSeleccionadas(List<Long> idsEntidadesBaseSeleccionadas) {
        return this.getEntidadesBaseAsignables()
                .stream().filter(eb -> idsEntidadesBaseSeleccionadas
                .stream().anyMatch(ebs -> ebs.equals(eb.getId())))
                .collect(Collectors.toList());
    }
}
