package proveedor;

public class Proveedor {

    private String nombre;
    private String documento;
    private DireccionPostal direccionPostal;
    private Boolean menorValor;

    public Proveedor(String nombre, String documento, DireccionPostal direccionPostal, boolean menorValor){
        this.nombre = nombre;
        this.documento = documento;
        this.direccionPostal = direccionPostal;
        this.menorValor = menorValor;
    }

    public Boolean getMenorValor() {
        return menorValor;
    }
}

