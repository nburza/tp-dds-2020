package controllers;

import egreso.Egreso;

import java.util.ArrayList;
import java.util.List;

public class Paginador {
    int pagActual;

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
        return Math.max(pagActual-1, 1);
    }

    public List<Egreso> paginar(List<Egreso> egresos){
        int registrosPorPagina = 5;

        if((registrosPorPagina*pagActual)-registrosPorPagina > egresos.size()){
            return new ArrayList<>();
        }else {
            return egresos.subList((registrosPorPagina*pagActual)-registrosPorPagina,
                    Math.min((registrosPorPagina*pagActual),egresos.size()));
        }
    }
}
