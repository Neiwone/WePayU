package br.ufal.ic.p2.wepayu.models;

public class EmpregadoAssalariado extends Empregado {


    public EmpregadoAssalariado() {
    }

    public EmpregadoAssalariado(String id, String nome, String endereco, Double salario) {
        super(id, nome, endereco);
        this.salario = salario;
        this.agendaPagamento = new AgendaPagamento();
        this.agendaPagamento.setPeriodoPagamento("mensal $");
    }
    public EmpregadoAssalariado(String nome, String endereco, Double salario) {
        super(nome, endereco);
        this.salario = salario;
        this.agendaPagamento = new AgendaPagamento();
        this.agendaPagamento.setPeriodoPagamento("mensal $");
    }


    public String getTipo() {
        return "assalariado";
    }

    public Double getSalario() {
        return salario;
    }

    @Override
    public EmpregadoAssalariado clone() {
        return (EmpregadoAssalariado) super.clone();
    }
}
