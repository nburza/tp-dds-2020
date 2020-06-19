package presupuesto;

import egreso.DocComercial;
import egreso.Egreso;
import egreso.Item;
import proveedor.Moneda;

import java.math.BigDecimal;
import java.util.List;

public class Presupuesto {
    private BigDecimal total;
    private List<Item> detalle;
    private List<DocComercial> documentosComerciales;
    private Egreso egreso;
    private Moneda moneda;

    public Presupuesto(BigDecimal total, List<Item> detalle, List<DocComercial> documentosComerciales, Egreso egreso, Moneda moneda){
        this.total = total;
        this.detalle = detalle;
        this.documentosComerciales = documentosComerciales;
        this.egreso = egreso;
        this.moneda = moneda;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<Item> getDetalle()
    {
        return detalle;
    }
}
