package entidadOrganizativa;

import egreso.Egreso;
import entidadOrganizativa.exceptions.MontoSuperadoException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("BEPM")
public class ReglaBloqueoEgresoPorMonto extends Regla {
    BigDecimal montoLimite;

    @Override
    public void verificarAgregadoDeEgreso(Entidad unaEntidad, Egreso unEgreso) {

        if (superaMontoLimite(unaEntidad, unEgreso))
            throw new MontoSuperadoException("No se puede agregar el egreso, se superó el monto limite");
    }

    private boolean superaMontoLimite(Entidad entidad, Egreso egreso) {
        BigDecimal montoFuturo = entidad.totalEgresos().add(egreso.totalEgreso());
        return montoFuturo.compareTo(montoLimite) > 0;
    }

    public ReglaBloqueoEgresoPorMonto(BigDecimal montoLimite) {

        this.montoLimite = montoLimite;
    }

    public ReglaBloqueoEgresoPorMonto() {
    }

    public void setMontoLimite(BigDecimal montoLimite) {
        this.montoLimite = montoLimite;
    }
}
