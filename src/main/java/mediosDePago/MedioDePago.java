package mediosDePago;

public abstract class MedioDePago {
    String descripcion;
    Long identificador;

    public MedioDePago(String descripcion, Long identificador){
        if(identificador==null){throw new IdentificadorNullException("El identificador del medio de pago no puede estar vacio");}
        this.descripcion = descripcion;
        this.identificador = identificador;
    }
}
