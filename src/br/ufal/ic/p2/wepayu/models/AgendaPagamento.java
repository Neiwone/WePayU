package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class AgendaPagamento implements Serializable, Cloneable {
    private String periodoPagamento;

    public AgendaPagamento() {

    }

    public double getWeeks() {
        String[] strSplit = periodoPagamento.split(" ");
        if (strSplit[0].equals("mensal"))
            return (52d/12D);
        if (strSplit[0].equals("semanal")) {
            if (strSplit.length == 2)
                return 1;
            else
                return (Integer.parseInt(strSplit[1]));
        }
        return 0;
    }


    public LocalDate getDaysSinceLastPay(LocalDate localDate) {
        String[] strSplit = periodoPagamento.split(" ");
        if (strSplit[0].equals("mensal"))
            return localDate.minusMonths(1);
        if (strSplit[0].equals("semanal")) {
            if (strSplit.length == 2)
                return localDate.minusDays(6);
            else
                return localDate.minusDays((Integer.parseInt(strSplit[1]) * 7L) - 1);
        }
        return localDate;
    }

    public String getPeriodoPagamento() {
        return periodoPagamento;
    }

    public void setPeriodoPagamento(String periodoPagamento) {
        this.periodoPagamento = periodoPagamento;
    }

    public boolean ehDia(LocalDate localDate) {
        String[] strSplit = periodoPagamento.split(" ");
        if (strSplit[0].equals("mensal")) {
            if (strSplit[1].equals("$"))
                return localDate == localDate.with(TemporalAdjusters.lastDayOfMonth());
            else
                return (Integer.parseInt(strSplit[1])) == localDate.getDayOfMonth();
        }
        if (strSplit[0].equals("semanal")) {
            if (strSplit.length == 2)
                return (Integer.parseInt(strSplit[1])) == localDate.getDayOfWeek().getValue();
            else
                return (((ChronoUnit.WEEKS.between(LocalDate.of(2005,1,1), localDate)) + 1) % Integer.parseInt(strSplit[1]) == 0) && ((Integer.parseInt(strSplit[2])) == localDate.getDayOfWeek().getValue());
        }
        return false;
    }





    @Override
    public AgendaPagamento clone() {
        try {
            return (AgendaPagamento) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
