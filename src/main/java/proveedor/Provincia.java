package proveedor;

import java.util.ArrayList;
import java.util.List;

public class Provincia {

    private String id;
    private String name;
    private List<Ciudad> cities = new ArrayList<>();

    public Provincia(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Provincia(String id, String name, List<Ciudad> cities) {
        this.id = id;
        this.name = name;
        this.cities = cities;
    }

    public String getId() {
        return id;
    }

    public List<Ciudad> getCiudades() {
        return cities;
    }
}
