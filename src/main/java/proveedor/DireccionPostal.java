package proveedor;

public class DireccionPostal {

    private String pais;
    private String provincia;
    private String ciudad;
    private String direccion;

    public DireccionPostal(String pais, String provincia, String ciudad, String direccion) {
        ValidadorDeUbicacion.getInstance().validarDireccionPostal(pais,provincia,ciudad);
        this.pais = pais;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
