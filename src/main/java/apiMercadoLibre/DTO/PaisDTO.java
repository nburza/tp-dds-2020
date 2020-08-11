package apiMercadoLibre.DTO;

import java.util.ArrayList;
import java.util.List;

public class PaisDTO {

    private String id;
    private String name;
    private List<ProvinciaDTO> states = new ArrayList<>();

    public PaisDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public PaisDTO(String id, String name, List<ProvinciaDTO> states) {
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

    public List<ProvinciaDTO> getProvincias() {
        return states;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStates(List<ProvinciaDTO> states) {
        this.states = states;
    }
}
