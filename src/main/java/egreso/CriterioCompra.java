package egreso;

import persistencia.EntidadPersistente;
import presupuesto.Presupuesto;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", length = 4)
public abstract class CriterioCompra extends EntidadPersistente {

    public List<Presupuesto> presupuestosQueCumplen(List<Presupuesto> presupuestos)
    {
        return presupuestos;
    }

}
