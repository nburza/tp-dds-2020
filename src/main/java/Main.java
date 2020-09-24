/*import egreso.*;
import presupuesto.Presupuesto;
import presupuesto.Proveedor;
import usuario.Usuario;
import java.math.BigDecimal;
import java.util.*;

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
        List<Item> items1 = new ArrayList<>();
        items1.add(item1);
        items1.add(item2);
        items1.add(item3);
        List<Item> items2 = new ArrayList<>();
        items2.add(item1);
        items2.add(item3);
        List<Item> items3 = new ArrayList<>();
        items3.add(item2);
        items3.add(item3);
        List<Item> items4 = new ArrayList<>();
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
        ValidadorDeEgresos.getInstance().validarTodos();

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
}*/