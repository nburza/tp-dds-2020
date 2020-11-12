package egreso;

import persistencia.RepositorioGenerico;

public class RepositorioDeItems extends RepositorioGenerico<Item> {

    private static final RepositorioDeItems instance = new RepositorioDeItems();

    public static RepositorioDeItems getInstance(){return  instance;}

    private RepositorioDeItems(){ }

    @Override
    protected Class<Item> getClase() {
        return Item.class;
    }
}
