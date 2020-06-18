package egreso;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import mediosDePago.MedioDePago;
import presupuesto.Presupuesto;
import usuario.Usuario;

public class Egreso
{
    private List<DocComercial> documentosComerciales;
    private MedioDePago medioDePago;
    private String idProveedor;
    private List<Item> items;
    private Date fecha;
    private List<Presupuesto> presupuestos = null;
    private  boolean requierePresupuesto = true;
    private List<Usuario> revisores = null;

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

    public Egreso(List<DocComercial> unosDC, MedioDePago unMedioDePago, String unIdProveedor, List<Item> unosItems, Date unaFecha)
    {
        this.documentosComerciales = unosDC;
        this.medioDePago = unMedioDePago;
        this.idProveedor = unIdProveedor;
        this.items = unosItems;
        this.fecha = unaFecha;
    }

    public Egreso(List<DocComercial> unosDC, MedioDePago unMedioDePago, String unIdProveedor, List<Item> unosItems, Date unaFecha, List<Presupuesto> presupuestos)
    {
        this.documentosComerciales = unosDC;
        this.medioDePago = unMedioDePago;
        this.idProveedor = unIdProveedor;
        this.items = unosItems;
        this.fecha = unaFecha;
        this.presupuestos = presupuestos;
    }

    public Egreso(List<DocComercial> unosDC, MedioDePago unMedioDePago, String unIdProveedor, List<Item> unosItems, Date unaFecha, boolean requierePresupuesto)
    {
        this.documentosComerciales = unosDC;
        this.medioDePago = unMedioDePago;
        this.idProveedor = unIdProveedor;
        this.items = unosItems;
        this.fecha = unaFecha;
        this.requierePresupuesto = requierePresupuesto;
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
}