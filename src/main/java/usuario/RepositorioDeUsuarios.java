package usuario;

import persistencia.RepositorioGenerico;

public class RepositorioDeUsuarios extends RepositorioGenerico<Usuario> {

    private static final RepositorioDeUsuarios instance = new RepositorioDeUsuarios();

    public static RepositorioDeUsuarios getInstance(){return  instance;}

    private RepositorioDeUsuarios(){ }

    @Override
    protected Class<Usuario> getClase() {
        return Usuario.class;
    }
}
