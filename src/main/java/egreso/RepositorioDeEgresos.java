package egreso;

import persistencia.RepositorioGenerico;

import java.util.ArrayList;
import java.util.List;

public class RepositorioDeEgresos extends RepositorioGenerico<Egreso> {

    private static final RepositorioDeEgresos instance = new RepositorioDeEgresos();

    public static RepositorioDeEgresos getInstance(){return  instance;}

    private RepositorioDeEgresos(){ }

    @Override
    protected Class<Egreso> getClase() {
        return Egreso.class;
    }
}