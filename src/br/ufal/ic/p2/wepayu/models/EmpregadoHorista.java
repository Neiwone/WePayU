package br.ufal.ic.p2.wepayu.models;
import java.io.Serializable;
import java.util.ArrayList;

public class EmpregadoHorista extends Empregado implements Serializable {
    protected String salarioPorHora;
    public ArrayList<CartaoDePonto> cartao;

    public EmpregadoHorista() {
    }

    public String getSalarioPorHora() {
        return salarioPorHora;
    }

    public ArrayList<CartaoDePonto> getCartao() {
        return cartao;
    }

    public void setCartao(ArrayList<CartaoDePonto> cartao) {
        this.cartao = cartao;
    }

    public void setSalarioPorHora(String salarioPorHora) {
        this.salarioPorHora = salarioPorHora;
    }

    public EmpregadoHorista(String id, String nome, String endereco, String salario) {
        super(id, nome, endereco);
        this.salarioPorHora = salario;
        this.cartao = new ArrayList<CartaoDePonto>();
    }

    public EmpregadoHorista(String nome, String endereco, String salario) {
        super(nome, endereco);
        this.salarioPorHora = salario;
        this.cartao = new ArrayList<CartaoDePonto>();
    }

    public String getTipo() {
        return "horista";
    }

    public String getSalario() {
        if(salarioPorHora.contains(","))
            return salarioPorHora;
        else
            return salarioPorHora + ",00";
    }
}
