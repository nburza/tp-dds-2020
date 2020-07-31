package egreso;

public class DocComercial
{
    private int numero;
    private TipoDocComercial tipoDC;

    public DocComercial(int unNumero, TipoDocComercial unTipoDC)
    {
        this.numero = unNumero;
        this.tipoDC = unTipoDC;
    }

    public TipoDocComercial getTipoDC(){
        return tipoDC;
}
}