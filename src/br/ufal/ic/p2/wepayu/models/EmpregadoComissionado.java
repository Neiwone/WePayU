package br.ufal.ic.p2.wepayu.models;
import java.io.Serializable;
import java.util.ArrayList;

public class EmpregadoComissionado extends Empregado implements Serializable {


    protected Double comissao;

    public EmpregadoComissionado() {
    }


    public ArrayList<ResultadoDeVenda> getVendas() {
        return vendas;
    }

    public void setVendas(ArrayList<ResultadoDeVenda> vendas) {
        this.vendas = vendas;
    }

    public ArrayList<ResultadoDeVenda> vendas;

    public EmpregadoComissionado(String id, String nome, String endereco, String salario, String comissao) {
        super(id, nome, endereco);
        this.salario = Double.parseDouble(salario.replace(',', '.'));
        this.comissao = Double.parseDouble(comissao.replace(',', '.'));
        this.vendas = new ArrayList<>();
    }

    public EmpregadoComissionado(String nome, String endereco, String salario, String comissao) {
        super(nome, endereco);
        this.salario = Double.parseDouble(salario.replace(',', '.'));
        this.comissao = Double.parseDouble(comissao.replace(',', '.'));
        this.vendas = new ArrayList<>();
    }

    public void setComissao(Double comissao) {
        this.comissao = comissao;
    }

    public String getTipo() {
        return "comissionado";
    }

    public Double getComissao() {
        return comissao;
    }


    public Double getSalario() {
        return salario;
    }
}
