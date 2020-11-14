package mediosDePago;

import persistencia.EntidadPersistente;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "forma", length = 2)

public abstract class MedioDePago extends EntidadPersistente {
    private String descripcion;
    private String identificador;

    public MedioDePago(String descripcion, String identificador){
        if(identificador==null){throw new IdentificadorNullException("El identificador del medio de pago no puede estar vacio");}
        this.descripcion = descripcion;
        this.identificador = identificador;
    }

    public MedioDePago() {
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getDescripcion() { return descripcion; }

    public String getIdentificador() {return identificador; }
}
