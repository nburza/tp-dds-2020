package entidadOrganizativa;

import entidadOrganizativa.exceptions.EntidadBaseNoIncorporableException;

public class ReglaEntidadBaseNoIncorporable extends Regla {

    @Override
    public void verificarSiEntidadBaseEsIncorporable() {
        throw new EntidadBaseNoIncorporableException("No se puede incorporar esta entidad base");
    }
}
