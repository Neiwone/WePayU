package br.ufal.ic.p2.wepayu.models;
import java.util.ArrayList;

public class EmpregadoComissionado extends Empregado  {


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

    public EmpregadoComissionado(String id, String nome, String endereco, Double salario, Double comissao) {
        super(id, nome, endereco);
        this.salario = salario;
        this.comissao = comissao;
        this.vendas = new ArrayList<>();
    }

    public EmpregadoComissionado(String nome, String endereco, Double salario, Double comissao) {
        super(nome, endereco);
        this.salario = salario;
        this.comissao = comissao;
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

    @Override
    public EmpregadoComissionado clone() {
        EmpregadoComissionado clone = (EmpregadoComissionado) super.clone();
        clone.vendas = new ArrayList<>();
        for (ResultadoDeVenda venda: this.vendas) {
            clone.vendas.add(venda.clone());
        }
        return clone;
    }
}
