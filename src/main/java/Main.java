import egreso.Egreso;
import egreso.Item;
import egreso.RepositorioDeValidaciones;
import presupuesto.Presupuesto;
import proveedor.Proveedor;
import usuario.Usuario;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Prueba");

        //Instancio usuarios de prueba
        Usuario usuario1 = new Usuario("usuario1", "calabaza");
        Usuario usuario2 = new Usuario("usuario2", "calabaza");

        //Instancio proveedor de prueba
        Proveedor proveedor1 = new Proveedor(null, null, null);

        //Instancio items de prueba
        Item item1 = new Item("harina", new BigDecimal("50"), 1, null);
        Item item2 = new Item("huevos", new BigDecimal("100"), 2, null);
        Item item3 = new Item("leche", new BigDecimal("150"), 3, null);

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
        Egreso egreso1 = new Egreso(null, null, items1, null, null);
        Egreso egreso2 = new Egreso(null, null, items2, null, null);
        Egreso egreso3 = new Egreso(null, null, items3, null, null);
        Egreso egreso4 = new Egreso(null, null, items4, null, false ,null);

        //Instancio presupuestos de prueba
        Presupuesto presupuesto1 = new Presupuesto(items1, null, egreso1, null, proveedor1);
        Presupuesto presupuesto2 = new Presupuesto(items2, null, egreso2, null, proveedor1); //presupuestos válidos
        Presupuesto presupuesto3 = new Presupuesto(items3, null, egreso3, null, proveedor1);

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
    }
}