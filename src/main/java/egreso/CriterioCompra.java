package egreso;

import presupuesto.Presupuesto;

import java.util.List;

public abstract class CriterioCompra {

    public List<Presupuesto> presupuestosQueCumplen(List<Presupuesto> presupuestos)
    {
        return presupuestos;
    }

}
