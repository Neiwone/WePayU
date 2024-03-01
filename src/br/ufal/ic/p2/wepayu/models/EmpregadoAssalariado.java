package br.ufal.ic.p2.wepayu.models;
import java.io.Serializable;
public class EmpregadoAssalariado extends Empregado implements Serializable {


    public EmpregadoAssalariado() {
    }

    public EmpregadoAssalariado(String id, String nome, String endereco, Double salario) {
        super(id, nome, endereco);
        this.salario = salario;
    }
    public EmpregadoAssalariado(String nome, String endereco, Double salario) {
        super(nome, endereco);
        this.salario = salario;
    }


    public String getTipo() {
        return "assalariado";
    }

    public Double getSalario() {
        return salario;
    }
}
