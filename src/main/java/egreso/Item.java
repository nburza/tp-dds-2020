package egreso;

import proveedor.DTO.MonedaDTO;
import proveedor.ValidadorDeMoneda;

import java.math.BigDecimal;

public class Item
{
    private String descripcion;
    private BigDecimal precioUnitario;
    private int cantidad;
    private String moneda;

    public BigDecimal precioTotal()
    {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad)) ;
    }

    public Item(String unaDescripcion, BigDecimal unPrecioUnitario, int unaCantidad, String moneda)
    {
        ValidadorDeMoneda.getInstance().validarMoneda(moneda);
        this.descripcion = unaDescripcion;
        this.precioUnitario = unPrecioUnitario;
        this.cantidad = unaCantidad;
        this.moneda = moneda;
    }
}