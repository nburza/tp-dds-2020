package usuario;

import persistencia.RepositorioGenerico;

import java.util.Optional;

public class RepositorioDeUsuarios extends RepositorioGenerico<Usuario> {

    private static final RepositorioDeUsuarios instance = new RepositorioDeUsuarios();

    public static RepositorioDeUsuarios getInstance(){return  instance;}

    private RepositorioDeUsuarios(){ }

    @Override
    protected Class<Usuario> getClase() {
        return Usuario.class;
    }

    @Override
    public void agregar(Usuario usuario) {
        if(getPorNombreDeUsuario(usuario.getNombreUsuario()).isPresent()) {
            throw new NombreDeUsuarioRepetidoException("El nombre de usuario ingresado ya esta en uso");
        }
        super.agregar(usuario);
    }

    public Optional<Usuario> getPorNombreDeUsuario(String nombreUsuario) {

        return getAllInstances()
                .stream()
                .filter(usuario -> usuario.getNombreUsuario().equals(nombreUsuario))
                .findFirst();
    }

}
