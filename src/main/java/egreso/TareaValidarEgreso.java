package egreso;

public class TareaValidarEgreso implements Tarea {

    private static final TareaValidarEgreso instance = new TareaValidarEgreso();

    public static TareaValidarEgreso getInstance(){return  instance;}

    /*LocalDate fechaDeLaTarea;
    boolean esTareaDiaria;


    public boolean puedeEjecutar(){
        return (this.esTareaDiaria ||fechaDeLaTarea.isEqual(LocalDate.now()));
    }*/

    public void ejecutar() {
        try {
            ValidadorDeEgresos.getInstance().validarTodos();
            System.out.println("Validaciones realizadas con exito");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
        }
    }
}

