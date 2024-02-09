package br.ufal.ic.p2.wepayu.models;

import java.util.ArrayList;

public class MembroSindicado {
    public String idMembro;
    public String taxaSindical;
    public ArrayList<TaxaServico> taxa;

    public MembroSindicado() {
        this.taxa = new ArrayList<TaxaServico>();
    }

    public String getIdMembro() {
        return idMembro;
    }

    public String getTaxaSindical() {
        return String.format("%.2f", Double.parseDouble(taxaSindical)).replace(".", ",");
    }
}
