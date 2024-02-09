package br.ufal.ic.p2.wepayu.models;

import java.util.ArrayList;

public class EmpregadoHorista extends Empregado{
    protected String salarioPorHora;
    public ArrayList<CartaoDePonto> cartao;


    public void setSalarioPorHora(String salarioPorHora) {
        this.salarioPorHora = salarioPorHora;
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
