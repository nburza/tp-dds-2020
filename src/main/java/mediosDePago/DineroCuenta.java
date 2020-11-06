package mediosDePago;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DC")

public class DineroCuenta extends  MedioDePago{
    public DineroCuenta(String descripcion, String identificador) {
        super(descripcion, identificador);
    }

    public DineroCuenta() {
    }
}
