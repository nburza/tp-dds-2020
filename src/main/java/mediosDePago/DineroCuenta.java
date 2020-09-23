package mediosDePago;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DC")

public class DineroCuenta extends  MedioDePago{
    public DineroCuenta(String descripcion, Long identificador) {
        super(descripcion, identificador);
    }
}
