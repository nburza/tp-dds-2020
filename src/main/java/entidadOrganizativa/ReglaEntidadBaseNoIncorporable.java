package entidadOrganizativa;

import entidadOrganizativa.exceptions.EntidadBaseNoIncorporableException;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("EBNI")
public class ReglaEntidadBaseNoIncorporable extends Regla {

    @Override
    public void verificarSiEntidadBaseEsIncorporable() {
        throw new EntidadBaseNoIncorporableException("No se puede incorporar esta entidad base");
    }
}
