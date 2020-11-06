package egreso;

import persistencia.EntidadPersistente;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "Etiquetas")
public class Etiqueta extends EntidadPersistente
{
    private String nombre;

    public Etiqueta (String nombre){
        this.nombre = nombre.toLowerCase();
    }

    public Etiqueta() {
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
