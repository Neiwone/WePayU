package br.ufal.ic.p2.wepayu.models;

import java.time.LocalDate;

public class TaxaServico {
    public LocalDate data;
    public Double valor;

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
