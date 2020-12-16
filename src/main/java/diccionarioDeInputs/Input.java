package diccionarioDeInputs;

import java.util.List;

public abstract class Input {

    public String getValorSimple() {
        return null;
    }

    public List<String> getValorMultiple() {
        return null;
    }

    public abstract boolean estaFaltante();
}
