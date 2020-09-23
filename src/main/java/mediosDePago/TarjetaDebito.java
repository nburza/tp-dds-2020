package mediosDePago;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TD")

public class TarjetaDebito extends MedioDePago {
    public TarjetaDebito(String descripcion, Long identificador) {
        super(descripcion, identificador);
    }
}
