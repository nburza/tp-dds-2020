package diccionarioDeInputs;

import java.util.HashMap;
import java.util.Map;

public class DiccionarioDeInputs {

    private Map<String,Input> inputs = new HashMap<>();

    public DiccionarioDeInputs() {
    }

    public Map<String, Input> getInputs() {
        return inputs;
    }

    public void put(String clave, String valor) {
        this.inputs.put(clave,new Input(valor,false));
    }

    public void putObligatorio(String clave, String valor) {
        this.inputs.put(clave, new Input(valor,true));
    }

    public void putObligatorioSi(String clave, String valor, boolean condicion) {
        this.inputs.put(clave, new Input(valor, condicion));
    }

    public String get(String clave) {
        return this.inputs.get(clave).getValor();
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
