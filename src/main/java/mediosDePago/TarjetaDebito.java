package mediosDePago;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TD")

public class TarjetaDebito extends MedioDePago {

    public TarjetaDebito(String descripcion, String identificador) {
        super(descripcion, identificador);
    }

    public TarjetaDebito() {}
}
