package egreso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mediosDePago.MedioDePago;
import presupuesto.Presupuesto;
import proveedor.Moneda;
import proveedor.Proveedor;
import usuario.Usuario;

public class Egreso
{
    private List<DocComercial> documentosComerciales = new ArrayList<>();
    private MedioDePago medioDePago;
    private Proveedor proveedor;
    private List<Item> items = new ArrayList<>();
    private Date fecha;
    private List<Presupuesto> presupuestos = new ArrayList<>();
    private  boolean requierePresupuesto = true;
    private List<Usuario> revisores = new ArrayList<>();
    private Moneda moneda;

    private static final int presupuestosRequeridos = 3;

    public BigDecimal totalEgreso()
    {
        BigDecimal total = new BigDecimal("0");

        for( int i=0; i < items.size(); i++)
        {
            total=total.add(items.get(i).precioTotal());
        }
        return total;
    }

    public Egreso(List<DocComercial> unosDC, MedioDePago unMedioDePago, Proveedor proveedor, List<Item> unosItems, Date unaFecha, Moneda moneda)
    {
        this.documentosComerciales = unosDC;
        this.medioDePago = unMedioDePago;
        this.proveedor = proveedor;
        this.items = unosItems;
        this.fecha = unaFecha;
        this.moneda = moneda;
        RepositorioDeEgresos.getInstance().agregarEgresosPendientes(this);
    }

    public Egreso(List<DocComercial> unosDC, MedioDePago unMedioDePago, Proveedor proveedor, List<Item> unosItems, Date unaFecha, boolean requierePresupuesto, Moneda moneda)
    {
        this.documentosComerciales = unosDC;
        this.medioDePago = unMedioDePago;
        this.proveedor = proveedor;
        this.items = unosItems;
        this.fecha = unaFecha;
        this.requierePresupuesto = requierePresupuesto;
        this.moneda = moneda;
        RepositorioDeEgresos.getInstance().agregarEgresosPendientes(this);
    }

    public void agregarPresupuesto(Presupuesto presupuesto) throws Exception {
        if(requierePresupuesto){
            presupuestos.add(presupuesto);
        }else throw new Exception("El egreso no requiere presupuesto");
    }

    public boolean esValido()
    {
        if(requierePresupuesto)
        {
            if(presupuestos.size() >= presupuestosRequeridos) //validación "A"
            {
                if(validarProveedor()) //validación "C"
                {
                    for (Presupuesto presupuesto : presupuestos) { //validación "B"
                        if (presupuesto.getDetalle().equals(items))
                            return true;
                    }
                }
            }
            return false;
        }
        else return true;
    }

    public List<Usuario> getRevisores()
    {
        return revisores;
    }

    public void agregarRevisor(Usuario usuario)
    {
        revisores.add(usuario);
    }

    public boolean validarProveedor(){
        BigDecimal menorValorPresupuesto = this.presupuestoMenorValor();
        BigDecimal mayorValorPresupuesto = this.presupuestoMayorValor();
        return (proveedor.getMenorValor() && this.totalEgreso().compareTo(menorValorPresupuesto) == 0) ||
                (!proveedor.getMenorValor() && this.totalEgreso().compareTo(mayorValorPresupuesto) == 0);
    }

    public BigDecimal presupuestoMenorValor(){
        BigDecimal valor = presupuestos.get(0).totalPresupuesto();
        for (int i = 0; i < presupuestos.size(); i++){
            if(valor.compareTo(presupuestos.get(i).totalPresupuesto()) == 1){
                valor = presupuestos.get(i).totalPresupuesto();
            }
        }
        return valor;
    }

    public BigDecimal presupuestoMayorValor(){
        BigDecimal valor = new BigDecimal("0");
        for (int i = 0; i < presupuestos.size(); i++){
            if(valor.compareTo(presupuestos.get(i).totalPresupuesto()) == -1){
                valor = presupuestos.get(i).totalPresupuesto();
            }
        }
        return valor;
    }

    public List<Item> getItems() {
        return items;
    }
}