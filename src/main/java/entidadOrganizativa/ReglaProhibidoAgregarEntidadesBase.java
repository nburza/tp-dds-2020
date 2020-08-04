package entidadOrganizativa;

import entidadOrganizativa.exceptions.EntidadSinEntidadesBaseException;

public class ReglaProhibidoAgregarEntidadesBase extends Regla {

    @Override
    public void verificarSiEntidadJuridicaPuedeAgregarEntidadesBase () {
        throw new EntidadSinEntidadesBaseException("Esta entidad juridica no puede incorporar entidades base");
    }
}
