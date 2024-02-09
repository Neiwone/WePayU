package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Sistema;

import java.io.Serializable;
import java.time.LocalDate;

public class CartaoDePonto implements Serializable {

    private String data;

    public void setData(String data) {
        this.data = data;
    }

    public CartaoDePonto() {
    }

    public void setHoras(Double horas) {
        this.horas = horas;
    }

    private Double horas;

    public CartaoDePonto(String data, Double horas) {
        this.data = data;
        this.horas = horas;
    }

    public String getData() {
        return data;
    }

    public Double getHoras() {
        return this.horas;
    }
}
