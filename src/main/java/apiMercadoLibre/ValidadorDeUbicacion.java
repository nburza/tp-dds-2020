package apiMercadoLibre;

import apiMercadoLibre.DTO.CiudadDTO;
import apiMercadoLibre.DTO.PaisDTO;
import apiMercadoLibre.DTO.ProvinciaDTO;
import apiMercadoLibre.exceptions.DireccionInvalidaException;

import java.util.List;

public class ValidadorDeUbicacion {

    private List<PaisDTO> paises;

    public ValidadorDeUbicacion(List<PaisDTO> paises) {
        this.paises = paises;
    }

    private boolean esDireccionValida(String paisString, String provinciaString, String ciudadString) {
        PaisDTO paisIndicado;
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
            throw new DireccionInvalidaException("La direccion postal es invalida");
        }
    }
}
