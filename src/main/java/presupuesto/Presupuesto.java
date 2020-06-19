package presupuesto;

import egreso.DocComercial;
import egreso.Egreso;
import egreso.Item;
import org.apache.commons.lang3.Validate;
import proveedor.Moneda;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Presupuesto {
    private List<Item> detalle = new ArrayList<>();
    private List<DocComercial> documentosComerciales = new ArrayList<>();
    private Egreso egreso;
    private Moneda moneda;

    public Presupuesto(List<Item> detalle, List<DocComercial> documentosComerciales, Egreso egreso, Moneda moneda){
        Validate.notNull(egreso, "El egreso no puede ser nulo");
        this.detalle = detalle;
        this.documentosComerciales = documentosComerciales;
        this.egreso = egreso;
        this.moneda = moneda;
        Validate.notNull(egreso, "El egreso no puede ser nulo");
    }

    public BigDecimal totalPresupuesto() {

        BigDecimal total = new BigDecimal("0");

        for( int i=0; i < detalle.size(); i++)
        {
            total=total.add(detalle.get(i).precioTotal());
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