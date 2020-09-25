package entidadOrganizativa;

import egreso.RepositorioDeEgresos;
import persistencia.RepositorioGenerico;

public class RepositorioDeOrganizaciones extends RepositorioGenerico<Organizacion> {

    private static final RepositorioDeOrganizaciones instance = new RepositorioDeOrganizaciones();

    public static RepositorioDeOrganizaciones getInstance(){return  instance;}

    private RepositorioDeOrganizaciones(){ }

    @Override
    protected Class<Organizacion> getClase() {
        return Organizacion.class;
    }
}
