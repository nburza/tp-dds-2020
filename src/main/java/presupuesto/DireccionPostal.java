package presupuesto;

import apiMercadoLibre.ServiceLocator;
import persistencia.EntidadPersistente;

import javax.persistence.Embeddable;
import javax.persistence.Entity;


@Embeddable
public class DireccionPostal  {

    private String pais;
    private String provincia;
    private String ciudad;
    private String direccion;

    public DireccionPostal(String pais, String provincia, String ciudad, String direccion) {
        ServiceLocator.getInstance().getValidadorDeUbicacion().validarDireccionPostal(pais,provincia,ciudad);
        this.pais = pais;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }

    public DireccionPostal() {
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
