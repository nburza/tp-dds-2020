package presupuesto;

import egreso.DocComercial;
import egreso.Egreso;
import egreso.Item;
import org.apache.commons.lang3.Validate;
import proveedor.Moneda;
import proveedor.Proveedor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class Presupuesto {
    private List<Item> detalle = new ArrayList<>();
    private List<DocComercial> documentosComerciales = new ArrayList<>();
    private Moneda moneda;
    private Proveedor proveedor;

    public Presupuesto(List<Item> detalle, List<DocComercial> documentosComerciales, Egreso egreso, Moneda moneda, Proveedor proveedor) throws Exception {
        Validate.notNull(egreso, "El egreso no puede ser nulo");
        this.detalle = detalle;
        this.documentosComerciales = documentosComerciales;
        this.moneda = moneda;
        this.proveedor = proveedor;

        egreso.agregarPresupuesto(this);
    }

    public BigDecimal totalPresupuesto() {

        BigDecimal total = new BigDecimal("0");
        BigDecimal totalFinal = detalle.stream()
                .map(x -> total.add(x.precioTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalFinal;
    }

    public List<Item> getDetalle()
    {
        return detalle;
    }

    public boolean esElMenor(List<Presupuesto> presupuestos)
    {
        return this == presupuestos.stream()
                .min(Comparator.comparing(Presupuesto::totalPresupuesto))
                .get();
    }
}