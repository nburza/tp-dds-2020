package diccionarioDeInputs;

public class InputSimple extends Input {

    private String valor;
    private boolean esObligatorio;

    public InputSimple(String value, boolean esObligatorio) {
        this.valor = value;
        this.esObligatorio = esObligatorio;
    }

    @Override
    public String getValorSimple() {
        return valor;
    }

    @Override
    public boolean estaFaltante() {
        return this.esObligatorio && this.valor.equals("");
    }
}
