package entidadOrganizativa;

import egreso.RepositorioDeEgresos;
import persistencia.RepositorioGenerico;

import java.util.Optional;

public class RepositorioDeOrganizaciones extends RepositorioGenerico<Organizacion> {

    private static final RepositorioDeOrganizaciones instance = new RepositorioDeOrganizaciones();

    public static RepositorioDeOrganizaciones getInstance(){return  instance;}

    private RepositorioDeOrganizaciones(){ }

    @Override
    protected Class<Organizacion> getClase() {
        return Organizacion.class;
    }

    public Optional<Organizacion> getOrganizacionDelUsuarioConId(Long idUsuario) {
        return getAllInstances().stream().filter(o -> o.contieneAlUsuarioConId(idUsuario)).findFirst();
    }
}
