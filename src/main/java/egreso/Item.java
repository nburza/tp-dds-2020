package egreso;

import java.math.BigDecimal;

public class Item
{
    private String descripcion;
    private BigDecimal precioUnitario;
    private int cantidad;

    public BigDecimal precioTotal()
    {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad)) ;
    }

    public Item(String unaDescripcion, BigDecimal unPrecioUnitario, int unaCantidad)
    {
        this.descripcion = unaDescripcion;
        this.precioUnitario = unPrecioUnitario;
        this.cantidad = unaCantidad;
    }
}