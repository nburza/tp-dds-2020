package mediosDePago;

import persistencia.RepositorioGenerico;

public class RepositorioDeMediosDePago extends RepositorioGenerico<MedioDePago> {

    private static final RepositorioDeMediosDePago instance = new RepositorioDeMediosDePago();

    public static RepositorioDeMediosDePago getInstance(){return  instance;}

    private RepositorioDeMediosDePago(){ }

    @Override
    protected Class<MedioDePago> getClase() {
        return MedioDePago.class;
    }
}
