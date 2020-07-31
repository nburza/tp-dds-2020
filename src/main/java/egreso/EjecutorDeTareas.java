package egreso;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

//El nombre puede cambiar si empieza a relizar mas tareas que dependan del tiempo
public class EjecutorDeTareas {

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private List<Tarea> tareasPendientes = new ArrayList<>();

    public void nuevaTarea(Tarea tarea){

        tareasPendientes.add(tarea);
    }

    public void ejecutarTareas(int hora, int minutos, int segundos, int diasIntervalo)
    {
        Runnable tareas = () -> tareasPendientes.forEach(Tarea::ejecutar);

        long delay = siguienteEjecucion(hora,minutos,segundos);
        executorService.scheduleAtFixedRate(tareas,delay,TimeUnit.DAYS.toSeconds(diasIntervalo),TimeUnit.SECONDS);
    }

    public void ejecutarTareas(int horas)
    {
        Runnable tareas = () -> tareasPendientes.forEach(Tarea::ejecutar);
        executorService.scheduleAtFixedRate(tareas,1, TimeUnit.HOURS.toSeconds(horas), TimeUnit.SECONDS);
    }

    private long siguienteEjecucion(int hora, int minutos, int segundos)
    {
        ZonedDateTime zonaAhora = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
        ZonedDateTime zonaSiguien = zonaAhora.withHour(hora).withMinute(minutos).withSecond(segundos);

        if (zonaAhora.compareTo(zonaSiguien) > 0) //si se llega al dia siguiente
        {
            zonaSiguien = zonaSiguien.plusDays(1);
        }
        Duration duracion = Duration.between(zonaAhora,zonaSiguien);

        return duracion.getSeconds();
    }

    public void pararEjecucion()
    {
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.DAYS); //Existe la posibilidad de que su tarea se atasque/bloquee y el usuario espere por siempre.
        } catch (InterruptedException ex) {
            Logger.getLogger(EjecutorDeTareas.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
}
