package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;

public class Correios extends MetodoPagamento implements Serializable {
    public Correios() {
    }

    public String getTipo() {
        return "correios";
    }
}
