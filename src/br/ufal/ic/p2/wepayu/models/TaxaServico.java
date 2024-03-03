package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
public class TaxaServico implements Serializable, Cloneable {
    public String data;
    public Double valor;

    public void setData(String data) {
        this.data = data;
    }

    public TaxaServico() {
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public TaxaServico(String data, Double valor) {
        this.data = data;
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public Double getValor() {
        return valor;
    }

    @Override
    public TaxaServico clone() {
        try {
            return (TaxaServico) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
