package diccionarioDeInputs;

import java.util.List;

public class InputMultiple extends Input {

    private List<String> valores;
    private boolean esObligatorio;

    public InputMultiple(List<String> valores, boolean esObligatorio) {
        this.valores = valores;
        this.esObligatorio = esObligatorio;
    }

    @Override
    public List<String> getValorMultiple() {
        return this.valores;
    }

    @Override
    public boolean estaFaltante() {
        return this.esObligatorio && this.valores.isEmpty();
    }
}
