package egreso;

import presupuesto.Presupuesto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CriterioMenorValor extends CriterioCompra{

    private static final CriterioMenorValor instance = new CriterioMenorValor();

    public static CriterioMenorValor getInstance(){return  instance;}

    private CriterioMenorValor(){ }

    public List<Presupuesto> presupuestosQueCumplen(List<Presupuesto> presupuestos)
    {
       return presupuestos.stream()
               .filter(x -> x.esElMenor(presupuestos))
               .collect(Collectors.toList());
    }
}

