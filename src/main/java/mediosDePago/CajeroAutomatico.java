package mediosDePago;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CA")

public class CajeroAutomatico extends MedioDePago{
    public CajeroAutomatico (String descripcion, String identificador){
        super(descripcion, identificador);
    }

    public CajeroAutomatico() {
    }
}
