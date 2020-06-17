package proveedor;

import java.util.ArrayList;
import java.util.List;

public class Pais {

    private String id;
    private String name;
    private List<Provincia> states = new ArrayList<>();

    public Pais(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Pais(String id, String name, List<Provincia> states) {
        this.id = id;
        this.name = name;
        this.states = states;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Provincia> getProvincias() {
        return states;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStates(List<Provincia> states) {
        this.states = states;
    }
}
