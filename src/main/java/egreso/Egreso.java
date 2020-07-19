package egreso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import mediosDePago.MedioDePago;
import presupuesto.Presupuesto;
import proveedor.Moneda;
import usuario.Usuario;

public class Egreso
{
    private List<DocComercial> documentosComerciales = new ArrayList<>();
    private MedioDePago medioDePago;
    private List<Item> items = new ArrayList<>();
    private Date fecha;
    private List<Presupuesto> presupuestos = new ArrayList<>();
    private boolean requierePresupuesto = true;
    private List<Usuario> revisores = new ArrayList<>();
    private Moneda moneda;
    private CriterioCompra criterioDeSeleccion = CriterioMenorValor.getInstance();
    private EstadoValidacion estado = EstadoValidacion.PENDIENTE;

    public EstadoValidacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoValidacion estado) {
        this.estado = estado;
    }





    private static final int presupuestosRequeridos = 3;

    public BigDecimal totalEgreso()
    {
        BigDecimal total = new BigDecimal("0");
        BigDecimal totalFinal = items.stream()
                .map(x -> total.add(x.precioTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalFinal;
    }

    public Egreso(List<DocComercial> unosDC, MedioDePago unMedioDePago, List<Item> unosItems, Date unaFecha, Moneda moneda)
    {
        this.documentosComerciales = unosDC;
        this.medioDePago = unMedioDePago;
        this.items = unosItems;
        this.fecha = unaFecha;
        this.moneda = moneda;
        RepositorioDeEgresos.getInstance().agregarEgresos(this);
    }

    public Egreso(List<DocComercial> unosDC, MedioDePago unMedioDePago, List<Item> unosItems, Date unaFecha, boolean requierePresupuesto, Moneda moneda)
    {
        this.documentosComerciales = unosDC;
        this.medioDePago = unMedioDePago;
        this.items = unosItems;
        this.fecha = unaFecha;
        this.requierePresupuesto = requierePresupuesto;
        this.moneda = moneda;
        RepositorioDeEgresos.getInstance().agregarEgresos(this);
    }

    public void agregarPresupuesto(Presupuesto presupuesto) throws Exception {
        if(requierePresupuesto){
            presupuestos.add(presupuesto);
        }else throw new Exception("El egreso no requiere presupuesto");
    }

    public boolean esValido()
    {
        return this.cumplePresupuestosMinimos() && //cantidad mínima de presupuestos requeridos
                this.estaBasadoEnUnPresupuesto() &&
                this.cumpleCriterioSeleccion();
    }/* si en un futuro hay que hacer validaciones sin presupuesto, se pone un IF de si requiere o no, y lo sacamos
        de cada una de estas validaciones. */
    public boolean cumplePresupuestosMinimos()
    {
        if(requierePresupuesto)
        {
            return presupuestos.size() >= presupuestosRequeridos;
        }
        return true;
    }

    public boolean estaBasadoEnUnPresupuesto()
    {
        if(requierePresupuesto)
        {
            return presupuestos.stream().anyMatch(x -> x.getDetalle().equals(items));
        }
        return true;
    }

    public boolean cumpleCriterioSeleccion()
    {
        if(requierePresupuesto)
        {
            return presupuestos.stream().anyMatch(x -> this.getCriterioDeSeleccion().presupuestosQueCumplen(presupuestos).contains(x));
        }
        return true;
    }

    public List<Usuario> getRevisores()
    {
        return revisores;
    }

    public void agregarRevisor(Usuario usuario)
    {
        revisores.add(usuario);
    }

    public List<Item> getItems() {
        return items;
    }

    public CriterioCompra getCriterioDeSeleccion() { return criterioDeSeleccion; }
}