package proveedor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ServicioUbicacionMercadoLibre {

    public List<Moneda> getMonedas() {
        String json = Client.create()
                .resource("https://api.mercadolibre.com/")
                .path("currencies/")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class)
                .getEntity(String.class);
        Type listType = new TypeToken<ArrayList<Moneda>>(){}.getType();
        return new Gson().fromJson(json, listType);
    }

    public List<Pais> getPaises() {
        String json = Client.create()
                .resource("https://api.mercadolibre.com/")
                .path("classified_locations/countries/")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class)
                .getEntity(String.class);
        Type listType = new TypeToken<ArrayList<Pais>>(){}.getType();
        return new Gson().fromJson(json, listType);
    }

    public  List<Provincia> getProvincias(Pais pais) {
        String json = Client.create()
                    .resource("https://api.mercadolibre.com/")
                    .path("classified_locations/countries/" + pais.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class)
                    .getEntity(String.class);
        Pais paisConProvincias = new Gson().fromJson(json, Pais.class);
        return paisConProvincias.getProvincias();
    }

    public List<Ciudad> getCiudades(Provincia provincia) {
        String json = Client.create()
                .resource("https://api.mercadolibre.com/")
                .path("classified_locations/states/" + provincia.getId())
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class)
                .getEntity(String.class);
        Provincia provinciaConCiudades = new Gson().fromJson(json, Provincia.class);
        return provinciaConCiudades.getCiudades();
    }
}
