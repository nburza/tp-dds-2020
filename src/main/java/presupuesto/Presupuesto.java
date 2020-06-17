package presupuesto;

import egreso.DocComercial;
import egreso.Egreso;
import egreso.Item;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Presupuesto {
    private BigDecimal total;
    private ArrayList<Item> detalles;
    private ArrayList<DocComercial> documentosComerciales;
    private Egreso egreso;

    public Presupuesto(BigDecimal total, ArrayList<Item> detalles, ArrayList<DocComercial> documentosComerciales, Egreso egreso){
        this.total = total;
        this.detalles = detalles;
        this.documentosComerciales = documentosComerciales;
        this.egreso = egreso;
    }
}
