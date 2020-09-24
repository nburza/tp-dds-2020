package egreso;

import apiMercadoLibre.ServiceLocator;
import persistencia.EntidadPersistente;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "Productos")
public class Producto extends EntidadPersistente
{

    private String descripcion;
    private BigDecimal precioUnitario;

    public Producto(String unaDescripcion, BigDecimal unPrecioUnitario)
    {
        this.descripcion = unaDescripcion;
        this.precioUnitario = unPrecioUnitario;
    }

    public BigDecimal getPrecioUnitario(){ return precioUnitario;}
}
