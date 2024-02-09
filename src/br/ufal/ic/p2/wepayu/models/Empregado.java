package br.ufal.ic.p2.wepayu.models;

public abstract class Empregado {
    protected String nome;
    protected String endereco;

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

    private MetodoPagamento metodoPagamento;

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public boolean getSindicalizado() {
        return (membroSindicado != null);
    }

    public MembroSindicado membroSindicado;

    public Empregado(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
        this.metodoPagamento = new EmMaos();
    }



    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public abstract String getSalario();

    public abstract String getTipo();




}
