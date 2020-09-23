package mediosDePago;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("E")
public class Efectivo extends MedioDePago{
    public Efectivo (String descripcion, Long identificador){
        super(descripcion, identificador);
    }
}
