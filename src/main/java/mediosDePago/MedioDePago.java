package mediosDePago;

import persistencia.EntidadPersistente;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "forma", length = 1)

public abstract class MedioDePago extends EntidadPersistente {
    private String descripcion;
    private Long identificador;

    public MedioDePago(String descripcion, Long identificador){
        if(identificador==null){throw new IdentificadorNullException("El identificador del medio de pago no puede estar vacio");}
        this.descripcion = descripcion;
        this.identificador = identificador;
    }
}
