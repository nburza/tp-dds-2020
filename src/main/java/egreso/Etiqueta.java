package egreso;

import java.util.Objects;

public class Etiqueta {
    private String nombre;

    public Etiqueta (String nombre){
        this.nombre = nombre.toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Etiqueta etiqueta = (Etiqueta) o;
        return Objects.equals(nombre, etiqueta.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    public String getNombre(){
        return nombre;
    }
}
