package egreso;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import mediosDePago.MedioDePago;

public class Egreso
{
    private ArrayList<DocComercial> documentosComerciales;
    private MedioDePago medioDePago;
    private String idProveedor;
    private ArrayList<Item> items;
    private Date fecha;

    public BigDecimal totalEgreso()
    {
        BigDecimal total = new BigDecimal("0");

        for( int i=0; i < items.size(); i++)
        {
            total=total.add(items.get(i).precioTotal());
        }
        return total;
    }

    public Egreso(ArrayList<DocComercial> unosDC, MedioDePago unMedioDePago, String unIdProveedor, ArrayList<Item> unosItems, Date unaFecha)
    {
        this.documentosComerciales = unosDC;
        this.medioDePago = unMedioDePago;
        this.idProveedor = unIdProveedor;
        this.items = unosItems;
        this.fecha = unaFecha;
    }
}