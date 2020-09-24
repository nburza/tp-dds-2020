package egreso;

import apiMercadoLibre.ServiceLocator;
import persistencia.EntidadPersistente;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "Items")
public class Item extends EntidadPersistente
{
    @ManyToOne
    private Producto producto;
    private int cantidad;
    private String moneda;

    public BigDecimal precioTotal()
    {
        return producto.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad)) ;
    }

    public Item(Producto unProducto, int unaCantidad, String moneda)
    {
        ServiceLocator.getInstance().getValidadorDeMoneda().validarMoneda(moneda);
        this.producto = unProducto;
        this.cantidad = unaCantidad;
        this.moneda = moneda;
    }
}