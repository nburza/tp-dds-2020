public class Item
{
    String descripcion;
    float precioUnitario;
    int cantidad;

    public float precioTotal()
    {
        return precioUnitario * cantidad;
    };

    public Item(String unaDescripcion, float unPrecioUnitario, int unaCantidad)
    {
        this.descripcion = unaDescripcion;
        this.precioUnitario = unPrecioUnitario;
        this.cantidad = unaCantidad;
    };
};