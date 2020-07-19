package proveedor;

public class Proveedor {

    private String nombre;
    private String documento;
    private DireccionPostal direccionPostal;

    public Proveedor(String nombre, String documento, DireccionPostal direccionPostal){
        this.nombre = nombre;
        this.documento = documento;
        this.direccionPostal = direccionPostal;
    }
}

