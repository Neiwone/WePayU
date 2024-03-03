package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class Database {
    private static Database instance;
    private LinkedHashMap<String, Empregado> empregados;

    private Database() throws IOException {
        //empregados = new LinkedHashMap<>();
        empregados = getFromXML();
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
        e.close();
    }

    private LinkedHashMap<String, Empregado> getFromXML() {

        BufferedInputStream file;
        try {file = new BufferedInputStream(new FileInputStream("data.xml"));}
        catch (FileNotFoundException e) {
            return new LinkedHashMap<>();
        }

        XMLDecoder d = new XMLDecoder(file);

        return (LinkedHashMap<String, Empregado>) d.readObject();
    }

    public void clear() {
        empregados.clear();
        try {
            Files.delete(Path.of("data.xml"));
        }catch (Exception e){
            System.out.println("Arquivo nao existe");
        }
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
