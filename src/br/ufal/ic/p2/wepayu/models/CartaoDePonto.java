package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;

public class CartaoDePonto implements Serializable, Cloneable {

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

    @Override
    public CartaoDePonto clone() {
        try {
            CartaoDePonto clone = (CartaoDePonto) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
