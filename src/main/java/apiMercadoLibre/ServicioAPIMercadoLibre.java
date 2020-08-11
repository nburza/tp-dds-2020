package apiMercadoLibre;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import apiMercadoLibre.DTO.*;

import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ServicioAPIMercadoLibre {

    public ServicioAPIMercadoLibre() {}

    public List<MonedaDTO> getMonedas() {
        String json = Client.create()
                .resource("https://api.mercadolibre.com/")
                .path("currencies/")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class)
                .getEntity(String.class);
        Type listType = new TypeToken<ArrayList<MonedaDTO>>(){}.getType();
        return new Gson().fromJson(json, listType);
    }

    public List<PaisDTO> getPaises() {
        String json = Client.create()
                .resource("https://api.mercadolibre.com/")
                .path("classified_locations/countries/")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class)
                .getEntity(String.class);
        Type listType = new TypeToken<ArrayList<PaisDTO>>(){}.getType();
        return new Gson().fromJson(json, listType);
    }

    public List<ProvinciaDTO> getProvincias(PaisDTO pais) {
        String json = Client.create()
                    .resource("https://api.mercadolibre.com/")
                    .path("classified_locations/countries/" + pais.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class)
                    .getEntity(String.class);
        PaisDTO paisConProvincias = new Gson().fromJson(json, PaisDTO.class);
        return paisConProvincias.getProvincias();
    }

    public List<CiudadDTO> getCiudades(ProvinciaDTO provinciaDTO) {
        String json = Client.create()
                .resource("https://api.mercadolibre.com/")
                .path("classified_locations/states/" + provinciaDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class)
                .getEntity(String.class);
        ProvinciaDTO provinciaDTOConCiudades = new Gson().fromJson(json, ProvinciaDTO.class);
        return provinciaDTOConCiudades.getCiudades();
    }

    public List<PaisDTO> getUbicaciones() {
        List<PaisDTO> paises = new ServicioAPIMercadoLibre().getPaises();
        paises.forEach(pais -> pais.setStates(new ServicioAPIMercadoLibre().getProvincias(pais)));
        paises.forEach(pais -> pais.getProvincias()
                .forEach(provincia -> provincia.setCiudades(new ServicioAPIMercadoLibre().getCiudades(provincia))));
        return paises;
    }
}
