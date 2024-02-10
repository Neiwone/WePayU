package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
import java.util.UUID;

public abstract class Empregado implements Serializable {

    protected String nome;
    protected String endereco;
    protected MembroSindicado membroSindicado;
    protected String id;
    protected MetodoPagamento metodoPagamento;


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




    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getSindicalizado() {
        return (membroSindicado != null);
    }




    public MembroSindicado getMembroSindicado() {
        return membroSindicado;
    }

    public void setMembroSindicado(MembroSindicado membroSindicado) {
        this.membroSindicado = membroSindicado;
    }

    public String getMetodo() {
        if(metodoPagamento instanceof EmMaos)
            return "emMaos";
        if(metodoPagamento instanceof Correios)
            return "correios";
        if(metodoPagamento instanceof Banco)
            return "banco";
        return "";
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }



    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getId() {
        return id;
    }



    public abstract String getSalario();

    public abstract String getTipo();




}
