package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
import java.util.UUID;

public abstract class Empregado implements Serializable, Cloneable {

    protected String id;
    protected String nome;
    protected String endereco;
    protected Double salario;
    protected MembroSindicado membroSindicado;
    protected MetodoPagamento metodoPagamento;

    // Constructors //
    public Empregado() {
    }
    public Empregado(String nome, String endereco) {
        setId(id = UUID.randomUUID().toString());
        this.nome = nome;
        this.endereco = endereco;
        this.metodoPagamento = new EmMaos();
    }
    public Empregado(String id, String nome, String endereco) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.metodoPagamento = new EmMaos();
    }


    // Getter & Setter - Tipo //

    public abstract String getTipo();

    // Getter & Setter - ID //

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    // Getter & Setter - Nome //

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter & Setter - Endereço //

    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    // Getter & Setter - Salario //

    public abstract Double getSalario();
    public void setSalario(Double salario) {
        this.salario = salario;
    }

    // Getter & Setter - Membro Sindicato //

    public MembroSindicado getMembroSindicado() {
        return membroSindicado;
    }
    public void setMembroSindicado(MembroSindicado membroSindicado) {
        this.membroSindicado = membroSindicado;
    }

    // Getter & Setter - Metodo de Pagamento //
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }




    public boolean getSindicalizado() {
        return (membroSindicado != null);
    }

    public String getMetodo() {
        return metodoPagamento.getTipo();
    }


    @Override
    public Empregado clone() {
        try {
            Empregado clone = (Empregado) super.clone();
            if (this.metodoPagamento != null)
                clone.metodoPagamento = metodoPagamento.clone();
            if (this.membroSindicado != null)
                clone.membroSindicado = membroSindicado.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
