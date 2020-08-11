package apiMercadoLibre.DTO;

import java.util.ArrayList;
import java.util.List;

public class ProvinciaDTO {

    private String id;
    private String name;
    private List<CiudadDTO> cities = new ArrayList<>();

    public ProvinciaDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProvinciaDTO(String id, String name, List<CiudadDTO> cities) {
        this.id = id;
        this.name = name;
        this.cities = cities;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CiudadDTO> getCiudades() {
        return cities;
    }

    public void setCiudades(List<CiudadDTO> cities) {
        this.cities = cities;
    }
}
