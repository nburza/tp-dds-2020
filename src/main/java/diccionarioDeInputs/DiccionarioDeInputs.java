package diccionarioDeInputs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiccionarioDeInputs {

    private Map<String, Input> inputs = new HashMap<>();

    public DiccionarioDeInputs() {
    }

    public Map<String, Input> getInputs() {
        return inputs;
    }

    public void putSimple(String clave, String valor) {
        this.inputs.put(clave,new InputSimple(valor,false));
    }

    public void putSimpleObligatorio(String clave, String valor) {
        this.inputs.put(clave, new InputSimple(valor,true));
    }

    public void putSimpleObligatorioSi(String clave, String valor, boolean condicion) {
        this.inputs.put(clave, new InputSimple(valor, condicion));
    }

    public void putMultiple(String clave, List<String> valores) {
        this.inputs.put(clave,new InputMultiple(valores,false));
    }

    public void putMultipleObligatorio(String clave, List<String> valores) {
        this.inputs.put(clave, new InputMultiple(valores,true));
    }

    public void putMultipleObligatorioSi(String clave, List<String> valores, boolean condicion) {
        this.inputs.put(clave, new InputMultiple(valores, condicion));
    }

    public String getSimple(String clave) {
        return this.inputs.get(clave).getValorSimple();
    }

    public List<String> getMultiple(String clave) {
        return this.inputs.get(clave).getValorMultiple();
    }

    public void chequearDatosFaltantes() {
        if(this.cantidadDeDatosFaltantes() == 1)
            throw new DatosFaltantesException("El campo " + this.getNombreDelCampoFaltante() + " es necesario");
        else if(this.cantidadDeDatosFaltantes() > 1)
            throw new DatosFaltantesException("Hay varios campos faltantes");
    }

    private int cantidadDeDatosFaltantes() {
        return Math.toIntExact(this.inputs.entrySet().stream().filter(e -> e.getValue().estaFaltante()).count());
    }

    private String getNombreDelCampoFaltante() {
        return this.inputs.entrySet().stream().filter(e -> e.getValue().estaFaltante()).findFirst().get().getKey();
    }
}
