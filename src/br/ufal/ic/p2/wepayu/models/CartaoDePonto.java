package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
import java.time.LocalDate;

public class CartaoDePonto implements Serializable {
    protected LocalDate data;

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setHoras(Double horas) {
        this.horas = horas;
    }

    protected Double horas;

    public CartaoDePonto(LocalDate data, Double horas) {
        this.data = data;
        this.horas = horas;
    }

    public LocalDate getData() {
        return this.data;
    }

    public Double getHoras() {
        return this.horas;
    }
}
