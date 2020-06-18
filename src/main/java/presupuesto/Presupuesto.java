package presupuesto;

import egreso.DocComercial;
import egreso.Egreso;
import egreso.Item;
import java.math.BigDecimal;
import java.util.List;

public class Presupuesto {
    private BigDecimal total;
    private List<Item> detalle;
    private List<DocComercial> documentosComerciales;
    private Egreso egreso;

    public Presupuesto(BigDecimal total, List<Item> detalle, List<DocComercial> documentosComerciales, Egreso egreso){
        this.total = total;
        this.detalle = detalle;
        this.documentosComerciales = documentosComerciales;
        this.egreso = egreso;
    }
}
