package diccionarioDeInputs;

public class Input {

    private String valor;
    private boolean esObligatorio;

    public Input(String value, boolean esObligatorio) {
        this.valor = value;
        this.esObligatorio = esObligatorio;
    }

    public String getValor() {
        return valor;
    }

    public boolean estaFaltante() {
        return this.esObligatorio && (this.valor == null || this.valor.equals(""));
    }
}
