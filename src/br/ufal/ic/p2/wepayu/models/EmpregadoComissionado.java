package br.ufal.ic.p2.wepayu.models;

import java.util.ArrayList;

public class EmpregadoComissionado extends Empregado{
    protected String salarioMensal;

    protected String comissao;

    public ArrayList<ResultadoDeVenda> vendas;

    public EmpregadoComissionado(String nome, String endereco, String salario, String comissao) {
        super(nome, endereco);
        this.salarioMensal = salario;
        this.comissao = comissao;
        this.vendas = new ArrayList<ResultadoDeVenda>();
    }

    public void setSalarioMensal(String salarioMensal) {
        this.salarioMensal = salarioMensal;
    }

    public void setComissao(String comissao) {
        this.comissao = comissao;
    }

    public String getTipo() {
        return "comissionado";
    }


    public String getComissao() {
        return comissao;
    }


    public String getSalario() {
        if(salarioMensal.contains(","))
            return salarioMensal;
        else
            return salarioMensal + ",00";
    }
}
