package apiMercadoLibre;

import apiMercadoLibre.DTO.MonedaDTO;
import apiMercadoLibre.exceptions.MonedaInvalidaException;

import java.util.List;

public class ValidadorDeMoneda {

    private List<MonedaDTO> monedasValidas;

    public ValidadorDeMoneda(List<MonedaDTO> monedasValidas) {
        this.monedasValidas = monedasValidas;
    }

    private boolean esMonedaValida(String moneda) {
        return monedasValidas.stream().anyMatch(x -> x.getDescription().equalsIgnoreCase(moneda));
    }

    public void validarMoneda(String moneda) {
        if(!esMonedaValida(moneda)) {
            throw new MonedaInvalidaException("La moneda ingresada no es valida");
        }
    }

}
