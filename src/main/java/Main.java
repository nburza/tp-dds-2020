import egreso.*;
import presupuesto.Presupuesto;
import proveedor.Proveedor;
import usuario.Usuario;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Prueba");

        //Instancio usuarios de prueba
        Usuario usuario1 = new Usuario("usuario1", "calabaza");
        Usuario usuario2 = new Usuario("usuario2", "calabaza");

        //Instancio proveedor de prueba
        Proveedor proveedor1 = new Proveedor(null, null, null);

        //Instancio items de prueba
        Item item1 = new Item("harina", new BigDecimal("50"), 1, "Peso argentino");
        Item item2 = new Item("huevos", new BigDecimal("100"), 2, "Peso argentino");
        Item item3 = new Item("leche", new BigDecimal("150"), 3, "Peso argentino");

        //Instancio listas de items de prueba
        List<Item> items1 = new ArrayList<Item>();
        items1.add(item1);
        items1.add(item2);
        items1.add(item3);
        List<Item> items2 = new ArrayList<Item>();
        items2.add(item1);
        items2.add(item3);
        List<Item> items3 = new ArrayList<Item>();
        items3.add(item2);
        items3.add(item3);
        List<Item> items4 = new ArrayList<Item>();
        items4.add(item1);
        items4.add(item3);

        //Instancio egresos de prueba y calculo sus totales
        Egreso egreso1 = new Egreso(null, null, items1, null, "Peso argentino");
        Egreso egreso2 = new Egreso(null, null, items2, null, "Peso argentino");
        Egreso egreso3 = new Egreso(null, null, items3, null, "Peso argentino");
        Egreso egreso4 = new Egreso(null, null, items4, null, false ,"Peso argentino");

        //Instancio presupuestos de prueba
        Presupuesto presupuesto1 = new Presupuesto(items1, null, egreso1, "Peso argentino", proveedor1);
        Presupuesto presupuesto2 = new Presupuesto(items2, null, egreso2, "Peso argentino", proveedor1); //presupuestos válidos
        Presupuesto presupuesto3 = new Presupuesto(items3, null, egreso3, "Peso argentino", proveedor1);

        //Cargo presupuestos a egresos

        egreso1.agregarPresupuesto(presupuesto2);
        egreso1.agregarPresupuesto(presupuesto3);

        egreso2.agregarPresupuesto(presupuesto1);
        egreso2.agregarPresupuesto(presupuesto3);

        egreso3.agregarPresupuesto(presupuesto2);

        //Cargo revisores a egresos
        egreso1.agregarRevisor(usuario1); //egreso válido!
        egreso2.agregarRevisor(usuario1); //egreso válido!
        egreso3.agregarRevisor(usuario2); //egreso inválido - presupuestos insuficientes!
        egreso4.agregarRevisor(usuario2); //egreso válido - no requiere presupuestos!

        //Ejecuto validaciones
        RepositorioDeValidaciones.getInstance().validarTodos();

        //Usuario consulta bandeja de entrada
        System.out.println(usuario1.consultarBandeja());
        System.out.println(usuario2.consultarBandeja());


        TareaEjemplo1 tareaEjemplo1 = new TareaEjemplo1();
        TareaEjemplo2 tareaEjemplo2 = new TareaEjemplo2();

        EjecutorDeTareas ejecutorDeTareas = new EjecutorDeTareas();

        ejecutorDeTareas.nuevaTarea(tareaEjemplo1);
        ejecutorDeTareas.nuevaTarea(tareaEjemplo2);
        ejecutorDeTareas.nuevaTarea(TareaValidarEgreso.getInstance());
        ejecutorDeTareas.ejecutarTareas(2);
        ejecutorDeTareas.ejecutarTareas(20,24,10,1);


        /*
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

        Runnable realizarValidaciones = () -> {
            try {
                RepositorioDeValidaciones.getInstance().validarTodos();
                System.out.println("Validaciones realizadas con exito.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        ZonedDateTime hoy = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime siguienteEjecucion = hoy.withHour(5).withMinute(0).withSecond(0);
        if (hoy.compareTo(siguienteEjecucion) > 0)
        {
            siguienteEjecucion = hoy.plusDays(1);
        }

        Duration duracion = Duration.between(hoy, siguienteEjecucion);
        long delay = duracion.getSeconds();

        ScheduledFuture<?> scheduledFuture = ses.scheduleAtFixedRate(realizarValidaciones,delay,TimeUnit.DAYS.toSeconds(1),TimeUnit.SECONDS);

        while (true){
            int count = 0
            System.out.println("count :" + count);
            Thread.sleep(1000);
            if (count == 5) {
                System.out.println("Count is 5, cancel the scheduledFuture!");
                scheduledFuture.cancel(true);
                ses.shutdown();
                break;
            }
        }
        */

        /*
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
        calendario.set(Calendar.HOUR_OF_DAY, 16);
        calendario.set(Calendar.MINUTE, 58);
        calendario.set(Calendar.SECOND, 45);
        calendario.set(Calendar.MILLISECOND, 0);
        Date fecha = calendario.getTime();

        TimerTask TareaValidaciones = new TimerTask() {
            @Override
            public void run() {
                try {
                    RepositorioDeValidaciones.getInstance().validarTodos();
                    System.out.println("Validaciones Realizadas");

                } catch (Exception ex) {
                    System.out.println("error al realizar las validaciones " + ex.getMessage());
                }
            }
        };

        DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fechaLimite = LocalDateTime.of(2020,7,22,18,53,0); //para frenar la tarea

        Timer time = new Timer();
        time.schedule(TareaValidaciones,fecha,TimeUnit.SECONDS.toMillis(1));

        while (true)
        {
            //Esto es porque LocalDateTime.now() devuelve los segundos del dia con todos los decimales, y es casi
            //imposible que maché
            String stringHoy = LocalDateTime.now().format(formatter); //con este formato los segundos no tiene decimales
            LocalDateTime hoy = LocalDateTime.parse(stringHoy, formatter); //podria comparar strings directamente, pero no
            Thread.sleep(1000); //esto es para que cada un segundo se ejecute el while

            if(hoy.isEqual(fechaLimite)){
                time.cancel();
                time.purge();
                break;
            }
        }
        */
    }
    public static class TareaEjemplo1 implements Tarea {
        public void ejecutar() {
            try {
                System.out.println("se ejecuta la tarea 1");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print(e.getMessage());
            }
        }
    }
    public static class TareaEjemplo2 implements Tarea {
        public void ejecutar() {
            try {
                System.out.println("se ejecuta la tarea 2");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print(e.getMessage());
            }
        }
    }
}