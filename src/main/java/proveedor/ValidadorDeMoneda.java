package proveedor;

import egreso.RepositorioDeEgresos;
import proveedor.DTO.MonedaDTO;

import java.util.ArrayList;
import java.util.List;

public class ValidadorDeMoneda {

    private List<MonedaDTO> monedasValidas = new ArrayList<>();

    private static final ValidadorDeMoneda instance = new ValidadorDeMoneda();

    public static ValidadorDeMoneda getInstance(){return  instance;}

    private ValidadorDeMoneda() {
        this.monedasValidas = new ServicioUbicacionMercadoLibre().getMonedas();
    }

    private boolean esMonedaValida(String moneda) {
        return monedasValidas.stream().anyMatch(x -> x.getDescription().equalsIgnoreCase(moneda));
    }

    public void validarMoneda(String moneda) {
        if(!esMonedaValida(moneda)) {
            throw new RuntimeException("La moneda ingresada no es valida");
        }
    }

}
