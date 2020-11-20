package egreso;

import apiMercadoLibre.ServiceLocator;
import persistencia.EntidadPersistente;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "Productos")
public class Producto extends EntidadPersistente {

    private String descripcion;

    public Producto(String unaDescripcion) {
        this.descripcion = unaDescripcion;
    }

    public Producto() {}

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion(){return descripcion;}
}
