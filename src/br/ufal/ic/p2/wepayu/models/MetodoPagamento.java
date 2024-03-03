package br.ufal.ic.p2.wepayu.models;
import java.io.Serializable;
public abstract class MetodoPagamento  implements Serializable, Cloneable {
    public abstract String getTipo();

    @Override
    public MetodoPagamento clone() {
        try {
            return (MetodoPagamento) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
