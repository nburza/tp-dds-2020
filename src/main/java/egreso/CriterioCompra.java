package egreso;

import persistencia.EntidadPersistente;
import presupuesto.Presupuesto;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.List;
import java.util.stream.Collectors;

public enum CriterioCompra {

    SIN_CRITERIO {
        public List<Presupuesto> presupuestosQueCumplen(List<Presupuesto> presupuestos) {
            return presupuestos;
        }
    },
    CRITERIO_MENOR_VALOR {
        public List<Presupuesto> presupuestosQueCumplen(List<Presupuesto> presupuestos) {
            return presupuestos.stream()
                    .filter(x -> x.esElMenor(presupuestos))
                    .collect(Collectors.toList());
        }
    };
    public abstract List<Presupuesto> presupuestosQueCumplen(List<Presupuesto> presupuestos);

}
