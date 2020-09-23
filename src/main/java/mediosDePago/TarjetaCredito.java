package mediosDePago;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TC")

public class TarjetaCredito extends MedioDePago{
    public TarjetaCredito (String descripcion, Long identificador){
        super(descripcion,identificador);
    }
}
