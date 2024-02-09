package br.ufal.ic.p2.wepayu.models;

import java.time.LocalDate;

public class CartaoDePonto {
    protected LocalDate data;
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
