package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.util.LinkedHashMap;
import java.util.Map;

public class Memento {

    private final LinkedHashMap<String, Empregado> employeesSnapshot;

    public Memento(LinkedHashMap<String, Empregado> employeesSnapshot) {
        this.employeesSnapshot = new LinkedHashMap<>();

        for (Map.Entry<String, Empregado> empregados : employeesSnapshot.entrySet()) {
            this.employeesSnapshot.put(empregados.getKey(), empregados.getValue().clone());
        }
    }

    public LinkedHashMap<String, Empregado> restore() {
        return this.employeesSnapshot;
    }

}
