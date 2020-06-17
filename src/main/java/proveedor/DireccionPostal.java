package proveedor;

public class DireccionPostal {

    private Pais pais;
    private Provincia provincia;
    private Ciudad ciudad;
    private String direccion;

    public DireccionPostal(Pais pais, Provincia provincia, Ciudad ciudad, String direccion) {
        this.pais = pais;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }
}
