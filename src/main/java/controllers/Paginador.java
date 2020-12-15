package controllers;

import egreso.Egreso;

import java.util.ArrayList;
import java.util.List;

public class Paginador {
    int pagActual;
    boolean tienePagSig = false;

    public Paginador (int pagActual){
        this.pagActual = pagActual;
    }
    public void setPagActual(int pagActual) {
        this.pagActual = pagActual;
    }

    public int getPagActual() {
        return pagActual;
    }

    public int getPagSig() {
        return pagActual+1;
    }

    public int getPagAnterior() {
        return pagActual-1;
    }

    public boolean getTienePagAnt(){
        return pagActual != 1;
    }

    public boolean getTienePagSig() {
        return tienePagSig;
    }

    public List<Egreso> paginar(List<Egreso> egresos){
        int registrosPorPagina = 5;
        this.tienePagSig = registrosPorPagina*pagActual<egresos.size();

        if((registrosPorPagina*pagActual)-registrosPorPagina > egresos.size()){
            return new ArrayList<>();
        }else {
            return egresos.subList((registrosPorPagina*pagActual)-registrosPorPagina,
                    Math.min((registrosPorPagina*pagActual),egresos.size()));
        }
    }
}
