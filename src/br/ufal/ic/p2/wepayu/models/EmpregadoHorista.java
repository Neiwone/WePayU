package br.ufal.ic.p2.wepayu.models;
import java.io.Serializable;
import java.util.ArrayList;

public class EmpregadoHorista extends Empregado implements Serializable {
    public ArrayList<CartaoDePonto> cartao;

    public EmpregadoHorista() {
    }


    public ArrayList<CartaoDePonto> getCartao() {
        return cartao;
    }

    public void setCartao(ArrayList<CartaoDePonto> cartao) {
        this.cartao = cartao;
    }

    public EmpregadoHorista(String id, String nome, String endereco, Double salario) {
        super(id, nome, endereco);
        this.salario = salario;
        this.cartao = new ArrayList<>();
    }

    public EmpregadoHorista(String nome, String endereco, Double salario) {
        super(nome, endereco);
        this.salario = salario;
        this.cartao = new ArrayList<>();
    }

    public String getTipo() {
        return "horista";
    }

    public Double getSalario() {
        return salario;
    }
}
