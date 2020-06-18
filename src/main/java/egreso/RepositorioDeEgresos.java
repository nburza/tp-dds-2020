package egreso;

import java.util.ArrayList;
import java.util.List;

class RepositorioDeEgresos{

    private List<Egreso> egresos;

    private List<Egreso> egresosPendientes;

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

    //list<egresos> getAll(){return egresos}

    public void validarTodos() throws Exception
    {
        if(egresosPendientes.size() == 0)
        {
            for (Egreso egresoPendiente : egresosPendientes) {
                if (egresoPendiente.esValido()) {
                    agregarEgresos(egresoPendiente);
                    eliminarEgresosPendientes(egresoPendiente);
                }
            }
        }
        else throw new Exception("No existen egresos pendientes de validación");
    }
}