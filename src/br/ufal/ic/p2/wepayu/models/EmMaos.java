package br.ufal.ic.p2.wepayu.models;
import java.io.Serializable;

public class EmMaos extends MetodoPagamento implements Serializable{
    public EmMaos() {
    }

    public String getTipo() {
        return "emMaos";
    }
}
