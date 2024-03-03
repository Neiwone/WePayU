package br.ufal.ic.p2.wepayu.models;
import java.util.ArrayList;

public class EmpregadoHorista extends Empregado  {
    public ArrayList<CartaoDePonto> cartao;

    public Double getAcumuladodescontos() {
        return acumuladodescontos;
    }

    public void setAcumuladodescontos(Double acumuladodescontos) {
        this.acumuladodescontos = acumuladodescontos;
    }

    private Double acumuladodescontos;

    public EmpregadoHorista() {
        this.acumuladodescontos = 0D;
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
        this.acumuladodescontos = 0D;
    }

    public EmpregadoHorista(String nome, String endereco, Double salario) {
        super(nome, endereco);
        this.salario = salario;
        this.cartao = new ArrayList<>();
        this.acumuladodescontos = 0D;
    }



    public String getTipo() {
        return "horista";
    }

    public Double getSalario() {
        return salario;
    }

    @Override
    public EmpregadoHorista clone() {
        EmpregadoHorista clone = (EmpregadoHorista) super.clone();
        clone.cartao = new ArrayList<>();
        for (CartaoDePonto cartao: this.cartao) {
            clone.cartao.add(cartao.clone());
        }
        // TODO: copy mutable state here, so the clone can't change the internals of the original
        return clone;
    }
}
