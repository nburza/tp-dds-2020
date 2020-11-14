package egreso;

import persistencia.RepositorioGenerico;

public class RepositorioDeEtiquetas extends RepositorioGenerico<Etiqueta> {

    private static final RepositorioDeEtiquetas instance = new RepositorioDeEtiquetas();

    public static RepositorioDeEtiquetas getInstance(){return  instance;}

    private RepositorioDeEtiquetas(){ }

    @Override
    protected Class<Etiqueta> getClase() {
        return Etiqueta.class;
    }
}
