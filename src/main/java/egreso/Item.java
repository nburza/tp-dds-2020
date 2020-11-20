package egreso;

import apiMercadoLibre.ServiceLocator;
import persistencia.EntidadPersistente;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "Items")
public class Item extends EntidadPersistente {
    @ManyToOne(cascade = CascadeType.ALL)
    private Producto producto;
    private BigDecimal precioUnitario;
    private int cantidad;
    private String moneda;

    public BigDecimal precioTotal() {
        return precioUnitario == null?
                BigDecimal.ZERO:
                getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad));
    }

    public Item(Producto unProducto, int unaCantidad, String moneda, BigDecimal unPrecioUnitario) {
//        ServiceLocator.getInstance().getValidadorDeMoneda().validarMoneda(moneda);
        this.producto = unProducto;
        this.precioUnitario = unPrecioUnitario;
        this.cantidad = unaCantidad;
        this.moneda = moneda;
    }

    public Item(Producto unProducto, int unaCantidad, String moneda) {
        //ServiceLocator.getInstance().getValidadorDeMoneda().validarMoneda(moneda);
        this.producto = unProducto;
        this.cantidad = unaCantidad;
        this.moneda = moneda;
    }

    public Item(){}

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public Producto getProducto() {return producto;}

    public int getCantidad() {return cantidad;}
}