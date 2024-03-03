package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static Database instance;
    private LinkedHashMap<String, Empregado> empregados;

    public List<String> getListOfPaydays() {
        return listOfPaydays;
    }

    private List<String> listOfPaydays;

    private Database() {
        getFromXML();
    }

    public static Database getInstance() throws IOException {

        if(instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void setData(LinkedHashMap<String, Empregado> empregadosSnapshot) {
        this.empregados = new LinkedHashMap<>();

        for (Map.Entry<String, Empregado> empregados : empregadosSnapshot.entrySet()) {
            this.empregados.put(empregados.getKey(), empregados.getValue().clone());
        }
    }

    public void saveToXML() throws IOException {
        Files.createFile(Path.of("data.xml"));
        BufferedOutputStream file = new BufferedOutputStream(new FileOutputStream("data.xml"));

        XMLEncoder e = new XMLEncoder(file);
        e.writeObject(empregados);
        e.writeObject(listOfPaydays);
        e.close();
    }

    private void getFromXML() {

        BufferedInputStream file = null;
        try {file = new BufferedInputStream(new FileInputStream("data.xml"));}
        catch (FileNotFoundException e) {
            empregados = new LinkedHashMap<>();
            listOfPaydays = new ArrayList<>();
            listOfPaydays.add("semanal 5");
            listOfPaydays.add("semanal 2 5");
            listOfPaydays.add("mensal $");
            return;
        }

        XMLDecoder d = new XMLDecoder(file);

        try { empregados = (LinkedHashMap<String, Empregado>) d.readObject(); }
        catch (Exception e) { empregados = new LinkedHashMap<>(); }

        try { listOfPaydays = (List<String>) d.readObject(); }
        catch (Exception e) {
            listOfPaydays = new ArrayList<>();
            listOfPaydays.add("semanal 5");
            listOfPaydays.add("semanal 2 5");
            listOfPaydays.add("mensal $");
        }
    }

    public void clear() {
        empregados.clear();
        listOfPaydays.clear();
        listOfPaydays.add("semanal 5");
        listOfPaydays.add("semanal 2 5");
        listOfPaydays.add("mensal $");
        try {
            Files.delete(Path.of("data.xml"));
        }catch (Exception ignored) {}
    }

    public LinkedHashMap<String, Empregado> getEmpregados() {
        return empregados;
    }

    public Empregado getEmpregado(String employeeID) {
        return empregados.get(employeeID);
    }

    public void removeEmpregado(String employeeID) {
        empregados.remove(employeeID);
    }


    public void addEmpregado(Empregado empregado) {
        empregados.put(empregado.getId(), empregado);
    }

    public void addEmpregado(String id, Empregado empregado) {
        empregados.put(id, empregado);
    }
}
