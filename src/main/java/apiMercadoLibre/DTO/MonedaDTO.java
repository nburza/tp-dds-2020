package apiMercadoLibre.DTO;

public class MonedaDTO {

    private String id;
    private String symbol;
    private String description;

    public MonedaDTO(String id, String symbol, String description) {
        this.id = id;
        this.symbol = symbol;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
