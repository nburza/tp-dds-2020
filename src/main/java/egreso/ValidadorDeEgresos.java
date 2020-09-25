package egreso;

import usuario.Usuario;
import java.util.Hashtable;

public class ValidadorDeEgresos {

    private static final ValidadorDeEgresos instance = new ValidadorDeEgresos();

    public static ValidadorDeEgresos getInstance(){return  instance;}

    private ValidadorDeEgresos(){ }

    public void validarTodos()
    {
            for (Egreso egresoPendiente : RepositorioDeEgresos.getInstance().getAllInstances())
            {
                egresoPendiente.validar();
            }

    }

    public Hashtable<Egreso, String> getAll(Usuario unUsuario)
    {
        Hashtable<Egreso, String> diccionarioDeValidaciones = new Hashtable<>();

        for(Egreso egreso : RepositorioDeEgresos.getInstance().getAllInstances())
        {
            if(egreso.esRevisor(unUsuario))
            {
                diccionarioDeValidaciones.put(egreso, egreso.getEstado().toString());
            }
        }
        return diccionarioDeValidaciones;
    }
}