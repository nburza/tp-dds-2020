package mediosDePago;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CA")

public class CajeroAutomatico extends MedioDePago{
    public CajeroAutomatico (String descripcion, Long identificador){
        super(descripcion, identificador);
    }
}
