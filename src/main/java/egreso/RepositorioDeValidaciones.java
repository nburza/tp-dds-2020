package egreso;

import usuario.Usuario;
import java.util.Hashtable;
import java.util.stream.Collectors;

public class RepositorioDeValidaciones{

    private static final RepositorioDeValidaciones instance = new RepositorioDeValidaciones();

    public static RepositorioDeValidaciones getInstance(){return  instance;}

    private RepositorioDeValidaciones(){ }

    public void validarTodos() throws Exception
    {
        if(RepositorioDeEgresos.getInstance().getEgresos().size() != 0)
        {
            for (Egreso egresoPendiente : RepositorioDeEgresos.getInstance().getEgresos())
            {
                if (egresoPendiente.esValido())
                {
                    egresoPendiente.setEstado(EstadoValidacion.VALIDO);
                }
            }
        }
        else throw new Exception("No existen egresos pendientes de validación");
    }

    public Hashtable<Egreso, String> getAll(Usuario unUsuario)
    {
        Hashtable<Egreso, String> DiccionarioDeValidaciones = new Hashtable<>();

        for(Egreso egreso : RepositorioDeEgresos.getInstance().getEgresos())
        {
            if(egreso.getRevisores().contains(unUsuario))
            {
                DiccionarioDeValidaciones.put(egreso, egreso.getEstado().toString());
            }
        }
        return DiccionarioDeValidaciones;
    }
}