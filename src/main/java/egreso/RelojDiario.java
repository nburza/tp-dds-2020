package egreso;

import java.util.Timer;

public class RelojDiario {
    Timer timer;

    public RelojDiario (){
        timer=new Timer();
        timer.schedule(new EjecutorDeTareas(),5000,86400000);
    }
}
