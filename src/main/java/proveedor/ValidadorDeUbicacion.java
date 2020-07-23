package proveedor;

import proveedor.DTO.CiudadDTO;
import proveedor.DTO.PaisDTO;
import proveedor.DTO.ProvinciaDTO;

import java.util.ArrayList;
import java.util.List;

public class ValidadorDeUbicacion {

    private List<PaisDTO> paises = new ArrayList<>();

    private static final ValidadorDeUbicacion instance = new ValidadorDeUbicacion();

    public static ValidadorDeUbicacion getInstance(){return  instance;}

    private ValidadorDeUbicacion() {
        if(paises.isEmpty())
            this.paises = new ServicioUbicacionMercadoLibre().getPaises();
            paises.forEach(pais -> pais.setStates(new ServicioUbicacionMercadoLibre().getProvincias(pais)));
            paises.forEach(pais -> pais.getProvincias()
                    .forEach(provincia -> provincia.setCiudades(new ServicioUbicacionMercadoLibre().getCiudades(provincia))));
    }

    private boolean esDireccionValida(String paisString, String provinciaString, String ciudadString) {
        PaisDTO paisIndicado = null;
        ProvinciaDTO provinciaIndicada = null;
        CiudadDTO ciudadIndicada = null;
        paisIndicado = this.paises.stream().filter(p -> p.getName().equals(paisString))
                .findAny()
                .orElse(null);
        if(paisIndicado != null) {
            provinciaIndicada = paisIndicado.getProvincias().stream()
                    .filter(p -> p.getName().equalsIgnoreCase(provinciaString))
                    .findAny()
                    .orElse(null);
            if(provinciaIndicada != null) {
                ciudadIndicada = provinciaIndicada.getCiudades().stream()
                        .filter(c -> c.getName().equalsIgnoreCase(ciudadString))
                        .findAny()
                        .orElse(null);
            }

        }
        return paisIndicado != null && provinciaIndicada != null && ciudadIndicada != null;
    }

    public void validarDireccionPostal(String paisString, String provinciaString, String ciudadString) {
        if(!esDireccionValida(paisString,provinciaString,ciudadString)) {
            throw new RuntimeException("La direccion postal es invalida");
        }
    }
}
