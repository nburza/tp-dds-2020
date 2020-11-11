package mediosDePago;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TC")

public class TarjetaCredito extends MedioDePago
{
    public TarjetaCredito (String descripcion, String identificador){
        super(descripcion,identificador);
    }

    public TarjetaCredito() {
    }
}
