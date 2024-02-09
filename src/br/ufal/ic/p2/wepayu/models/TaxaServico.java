package br.ufal.ic.p2.wepayu.models;

import java.time.LocalDate;
import java.io.Serializable;
public class TaxaServico implements Serializable {
    public LocalDate data;
    public Double valor;

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public TaxaServico(LocalDate data, Double valor) {
        this.data = data;
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public double getValor() {
        return valor;
    }
}
