package egreso;

import java.util.ArrayList;
import java.util.List;

public class RepositorioDeEgresos{

    private List<Egreso> egresos = new ArrayList<>();
    private static final RepositorioDeEgresos instance = new RepositorioDeEgresos();

    public static RepositorioDeEgresos getInstance(){return  instance;}

    private RepositorioDeEgresos(){ }

    public void agregarEgresos(Egreso unEgreso)
    {
        egresos.add(unEgreso);
    }

    public void eliminarEgresos(Egreso unEgreso)
    {
        egresos.remove(unEgreso);
    }

    public List<Egreso> getEgresos() {
        return egresos;
    }
}