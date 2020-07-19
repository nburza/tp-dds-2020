package presupuesto;

import egreso.DocComercial;
import egreso.Egreso;
import egreso.Item;
import org.apache.commons.lang3.Validate;
import proveedor.Moneda;
import proveedor.Proveedor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Presupuesto {
    private List<Item> detalle = new ArrayList<>();
    private List<DocComercial> documentosComerciales = new ArrayList<>();
    private Egreso egreso;
    private Moneda moneda;
    private Proveedor proveedor;

    public Presupuesto(List<Item> detalle, List<DocComercial> documentosComerciales, Egreso egreso, Moneda moneda, Proveedor proveedor){
        Validate.notNull(egreso, "El egreso no puede ser nulo");
        this.detalle = detalle;
        this.documentosComerciales = documentosComerciales;
        this.egreso = egreso;
        this.moneda = moneda;
        this.proveedor = proveedor;
    }

    public BigDecimal totalPresupuesto() {

        BigDecimal total = new BigDecimal("0");

        for (Item item : detalle) {
            total = total.add(item.precioTotal());
        }
        return total;
    }

    public List<Item> getDetalle()
    {
        return detalle;
    }

    public Egreso getEgreso() {
        return egreso;
    }
}