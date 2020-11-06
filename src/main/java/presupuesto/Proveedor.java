package presupuesto;

import persistencia.EntidadPersistente;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Proveedor extends EntidadPersistente {

    private String nombre;
    private String documento;
    @Embedded
    private DireccionPostal direccionPostal;

    public Proveedor(String nombre, String documento, DireccionPostal direccionPostal){
        this.nombre = nombre;
        this.documento = documento;
        this.direccionPostal = direccionPostal;
    }

    public Proveedor() {
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void setDireccionPostal(DireccionPostal direccionPostal) {
        this.direccionPostal = direccionPostal;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public DireccionPostal getDireccionPostal() {
        return direccionPostal;
    }
}

