package egreso;

import usuario.Usuario;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class RepositorioDeValidaciones{

    private Hashtable<Egreso, String> validaciones = new Hashtable<>();
    private static final RepositorioDeValidaciones instance = new RepositorioDeValidaciones();

    public static RepositorioDeValidaciones getInstance(){return  instance;}

    private RepositorioDeValidaciones(){ }

    public Hashtable<Egreso, String> getAll(Usuario unUsuario)
    {
        Hashtable<Egreso, String> DiccionarioDeValidaciones = new Hashtable<>();

        for(Egreso egresoPendiente : RepositorioDeEgresos.getInstance().getEgresosPendientes())
        {
            if(egresoPendiente.getRevisores().contains(unUsuario))
            {
                DiccionarioDeValidaciones.put(egresoPendiente, "Inválido");
            }
        }
        for(Egreso egreso : RepositorioDeEgresos.getInstance().getEgresos())
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
        if(RepositorioDeEgresos.getInstance().getEgresosPendientes().size() != 0)
        {
            List<Egreso> egresosPendientesAUX = new ArrayList<>();
            for (Egreso egresoPendiente : RepositorioDeEgresos.getInstance().getEgresosPendientes())
            {
                if (egresoPendiente.esValido())
                {
                    RepositorioDeEgresos.getInstance().agregarEgresos(egresoPendiente);
                    egresosPendientesAUX.add(egresoPendiente);
                }
            }
            RepositorioDeEgresos.getInstance().getEgresosPendientes().removeAll(egresosPendientesAUX);
        }
        else throw new Exception("No existen egresos pendientes de validación");
    }
}