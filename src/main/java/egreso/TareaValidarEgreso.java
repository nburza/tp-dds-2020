package egreso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimerTask;

public class TareaValidarEgreso implements Tarea {
    LocalDate fechaDeLaTarea;
    boolean esTareaDiaria;


    public boolean puedeEjecutar(){
        return (this.esTareaDiaria ||fechaDeLaTarea.isEqual(LocalDate.now()));
    }

    public void ejecutarSiCorresponde() {
        if (this.puedeEjecutar()){
        try {
            RepositorioDeValidaciones.getInstance().validarTodos();
            } catch (Exception e)
            {
            e.printStackTrace();
            System.out.print(e.getMessage());
            }

        }}
    }

