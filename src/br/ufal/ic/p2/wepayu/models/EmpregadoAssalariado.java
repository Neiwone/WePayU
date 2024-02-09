package br.ufal.ic.p2.wepayu.models;

public class EmpregadoAssalariado extends Empregado{

    private String salarioMensal;



    public EmpregadoAssalariado(String nome, String endereco, String salario) {
        super(nome, endereco);
        this.salarioMensal = salario;
    }

    public String getTipo() {
        return "assalariado";
    }

    public void setSalarioMensal(String salarioMensal) {
        this.salarioMensal = salarioMensal;
    }

    public String getSalario() {
        if(salarioMensal.contains(","))
            return salarioMensal;
        else
            return salarioMensal + ",00";
    }
}
