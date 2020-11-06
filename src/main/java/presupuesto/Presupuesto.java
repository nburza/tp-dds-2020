package presupuesto;

import egreso.DocComercial;
import egreso.Egreso;
import egreso.Item;
import org.apache.commons.lang3.Validate;
import apiMercadoLibre.ServiceLocator;
import persistencia.EntidadPersistente;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "Presupuestos")
public class Presupuesto extends EntidadPersistente
{
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "presupuesto_id")
    private List<Item> detalle = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "presupuesto_id")
    private List<DocComercial> documentosComerciales = new ArrayList<>();
    private String moneda;
    @ManyToOne(cascade = CascadeType.ALL)
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

    public Presupuesto() {
    }

    public void setDetalle(List<Item> detalle) {
        this.detalle = detalle;
    }

    public void setDocumentosComerciales(List<DocComercial> documentosComerciales) {
        this.documentosComerciales = documentosComerciales;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Proveedor getProveedor() {
        return proveedor;
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