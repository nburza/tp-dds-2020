package usuario;

import persistencia.RepositorioGenerico;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    public static boolean estaLogueado(Request request, Response response) {
        Usuario usuario = getUsuarioLogueado(request);

        return usuario != null;
    }

    public static Usuario getUsuarioLogueado(Request request) {
        Long idUsuario = request.session().attribute("idUsuario");

        Usuario usuario = null;

        if(idUsuario != null){
            usuario = RepositorioDeUsuarios.getInstance().getPorId(idUsuario).get();
        }

        return usuario;
    }

}
