package br.ufal.ic.p2.wepayu.models;
import java.io.Serializable;
public class ResultadoDeVenda implements Serializable {
    private String data;
    private Double valor;

    public ResultadoDeVenda(String data, Double valor) {
        this.data = data;
        this.valor = valor;
    }

    public ResultadoDeVenda() {
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getData() {
        return this.data;
    }

    public Double getValor() {
        return valor;
    }
}
