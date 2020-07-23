package egreso;

import java.util.List;
import java.util.TimerTask;

//El nombre puede cambiar si empieza a relizar mas tareas que dependan del tiempo
public class EjecutorDeTareas extends TimerTask {
    List<Tarea> tareasPendientes;

    public void nuevaTarea(Tarea tarea){

        tareasPendientes.add(tarea);
    }


    @Override
    public void run() {
        tareasPendientes.forEach(Tarea::ejecutarSiCorresponde);

    }

}
