package apiMercadoLibre;

public class ServiceLocator {

    private ValidadorDeMoneda validadorDeMoneda;
    private ValidadorDeUbicacion validadorDeUbicacion;

    private static final ServiceLocator instance = new ServiceLocator();

    public static ServiceLocator getInstance(){return  instance;}

    private ServiceLocator() {}

    public ValidadorDeMoneda getValidadorDeMoneda() {
        return validadorDeMoneda;
    }

    public void setValidadorDeMoneda(ValidadorDeMoneda validadorDeMoneda) {
        this.validadorDeMoneda = validadorDeMoneda;
    }

    public ValidadorDeUbicacion getValidadorDeUbicacion() {
        return validadorDeUbicacion;
    }

    public void setValidadorDeUbicacion(ValidadorDeUbicacion validadorDeUbicacion) {
        this.validadorDeUbicacion = validadorDeUbicacion;
    }
}
