package egreso;

import presupuesto.Presupuesto;

import java.util.ArrayList;
import java.util.List;

public class CriterioMenorValor extends CriterioCompra{

    private static final CriterioMenorValor instance = new CriterioMenorValor();

    public static CriterioMenorValor getInstance(){return  instance;}

    private CriterioMenorValor(){ }

    public List<Presupuesto> presupuestosQueCumplen(List<Presupuesto> presupuestos)
    {
        List<Presupuesto> presupuestosCorrectos = new ArrayList<Presupuesto>();
        Presupuesto presupuestoCorrecto = presupuestos.get(0);
        presupuestosCorrectos.add(presupuestoCorrecto);
        for(Presupuesto presupuesto : presupuestos){
            if (presupuesto.totalPresupuesto().compareTo(presupuestoCorrecto.totalPresupuesto()) < 0);
                presupuestoCorrecto = presupuesto;
        }
        return presupuestosCorrectos;
    }
}
