package egreso;

import usuario.Usuario;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class RepositorioDeEgresos{

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

    public Hashtable<Egreso, String> getAll(Usuario unUsuario)
    {
        Hashtable<Egreso, String> DiccionarioDeValidaciones = new Hashtable<>();

        for(Egreso egresoPendiente : egresosPendientes)
        {
            if(egresoPendiente.getRevisores().contains(unUsuario))
            {
                DiccionarioDeValidaciones.put(egresoPendiente, "Inválido");
            }
        }
        for(Egreso egreso : egresos)
        {
            if(egreso.getRevisores().contains(unUsuario))
            {
                DiccionarioDeValidaciones.put(egreso, "Válido");
            }
        }
        return DiccionarioDeValidaciones;
    }

    public void validarTodos() throws Exception
    {
        if(egresosPendientes.size() == 0)
        {
            for (Egreso egresoPendiente : egresosPendientes)
            {
                if (egresoPendiente.esValido())
                {
                    agregarEgresos(egresoPendiente);
                    eliminarEgresosPendientes(egresoPendiente);
                }
            }
        }
        else throw new Exception("No existen egresos pendientes de validación");
    }
}