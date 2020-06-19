package egreso;

import proveedor.Moneda;
import java.math.BigDecimal;

public class Item
{
    private String descripcion;
    private BigDecimal precioUnitario;
    private int cantidad;
    private Moneda moneda;

    public BigDecimal precioTotal()
    {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad)) ;
    }

    public Item(String unaDescripcion, BigDecimal unPrecioUnitario, int unaCantidad, Moneda moneda)
    {
        this.descripcion = unaDescripcion;
        this.precioUnitario = unPrecioUnitario;
        this.cantidad = unaCantidad;
        this.moneda = moneda;
    }
}