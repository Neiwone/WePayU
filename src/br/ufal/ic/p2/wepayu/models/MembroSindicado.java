package br.ufal.ic.p2.wepayu.models;
import java.io.Serializable;
import java.util.ArrayList;

public class MembroSindicado implements Serializable {
    public String idMembro;
    public String taxaSindical;
    public ArrayList<TaxaServico> taxa;

    public void setIdMembro(String idMembro) {
        this.idMembro = idMembro;
    }

    public void setTaxaSindical(String taxaSindical) {
        this.taxaSindical = taxaSindical;
    }

    public ArrayList<TaxaServico> getTaxa() {
        return taxa;
    }

    public void setTaxa(ArrayList<TaxaServico> taxa) {
        this.taxa = taxa;
    }

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
