package egreso;

import usuario.Usuario;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class RepositorioDeEgresos{

    private List<Egreso> egresos;
    private List<Egreso> egresosPendientes;
    private static final RepositorioDeEgresos instance = new RepositorioDeEgresos(null, null);

    public static RepositorioDeEgresos getInstance(){return  instance;}

    private RepositorioDeEgresos(List<Egreso> egresos, List<Egreso> egresosPendientes)
    {
        this.egresos = egresos;
        this.egresosPendientes = egresosPendientes;
    }

    public void agregarEgresos(Egreso unEgreso)
    {
        egresos.add(unEgreso);
    }

    public void eliminarEgresos(Egreso unEgreso)
    {
        egresos.remove(unEgreso);
    }

    public void agregarEgresosPendientes(Egreso unEgreso)
    {
        egresosPendientes.add(unEgreso);
    }

    public void eliminarEgresosPendientes(Egreso unEgreso)
    {
        egresosPendientes.remove(unEgreso);
    }

    public List<Egreso> getEgresos() {
        return egresos;
    }

    public List<Egreso> getEgresosPendientes() {
        return egresosPendientes;
    }
}