import java.util.Date;
import java.util.ArrayList;
import mediosDePago.MedioDePago;

public class Egreso
{
    ArrayList<DocComercial> documentosComerciales;
    MedioDePago medioDePago;
    String idProveedor;
    static ArrayList<Item> items;
    Date fecha;

    public static float totalEgreso()
    {
        float total = 0;

        for( int i=0; i < items.size(); i++)
        {
            total += items.get(i).precioTotal();
        };
        return total;
    };

    public Egreso(ArrayList<DocComercial> unosDC, MedioDePago unMedioDePago, String unIdProveedor, ArrayList<Item> unosItems, Date unaFecha)
    {
        this.documentosComerciales = unosDC;
        this.medioDePago = unMedioDePago;
        this.idProveedor = unIdProveedor;
        this.items = unosItems;
        this.fecha = unaFecha;
    };
};