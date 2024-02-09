package br.ufal.ic.p2.wepayu.models;
import java.io.Serializable;
public class EmpregadoAssalariado extends Empregado implements Serializable {

    private String salarioMensal;

    public String getSalarioMensal() {
        return salarioMensal;
    }

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
