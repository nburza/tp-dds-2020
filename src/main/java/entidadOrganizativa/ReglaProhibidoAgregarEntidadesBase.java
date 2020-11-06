package entidadOrganizativa;

import entidadOrganizativa.exceptions.EntidadSinEntidadesBaseException;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PAEB")
public class ReglaProhibidoAgregarEntidadesBase extends Regla {

    public ReglaProhibidoAgregarEntidadesBase() {
    }

    @Override
    public void verificarSiEntidadJuridicaPuedeAgregarEntidadesBase () {
        throw new EntidadSinEntidadesBaseException("Esta entidad juridica no puede incorporar entidades base");
    }
}
