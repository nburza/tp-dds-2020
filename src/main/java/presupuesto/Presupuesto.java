package presupuesto;

import egreso.DocComercial;
import egreso.Egreso;
import egreso.Item;
import org.apache.commons.lang3.Validate;
import apiMercadoLibre.ServiceLocator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Presupuesto {
    private List<Item> detalle = new ArrayList<>();
    private List<DocComercial> documentosComerciales = new ArrayList<>();
    private String moneda;
    private Proveedor proveedor;

    public Presupuesto(List<Item> detalle, List<DocComercial> documentosComerciales, Egreso egreso, String moneda, Proveedor proveedor) throws Exception {
        Validate.notNull(egreso, "El egreso no puede ser nulo");
        ServiceLocator.getInstance().getValidadorDeMoneda().validarMoneda(moneda);
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