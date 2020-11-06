package egreso;

import persistencia.EntidadPersistente;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class DocComercial extends EntidadPersistente
{
    private int numero;
    @Enumerated(EnumType.STRING)
    private TipoDocComercial tipoDC;

    public DocComercial(int unNumero, TipoDocComercial unTipoDC)
    {
        this.numero = unNumero;
        this.tipoDC = unTipoDC;
    }

    public DocComercial() {
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setTipoDC(TipoDocComercial tipoDC) {
        this.tipoDC = tipoDC;
    }

    public TipoDocComercial getTipoDC(){
        return tipoDC;
}
}