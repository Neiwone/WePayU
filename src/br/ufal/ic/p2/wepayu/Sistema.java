package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.models.*;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class Sistema {
    public static HashMap<String, Empregado> empregados;

    public static Integer tamanho;

    public Sistema() {
        empregados = new HashMap<>();
        tamanho = 0;
    }

    public static void saveToXML() throws IOException {
        Files.createFile(Path.of("data.xml"));
        BufferedOutputStream file = new BufferedOutputStream(new FileOutputStream("data.xml"));

        XMLEncoder e = new XMLEncoder(file);
        e.writeObject(empregados);
        e.writeObject(tamanho);
        e.close();
    }

    public static void getFromXML() throws FileNotFoundException {

        BufferedInputStream file = new BufferedInputStream(new FileInputStream("data.xml"));

        XMLDecoder d = new XMLDecoder(file);
        empregados =  (HashMap<String, Empregado>) d.readObject();
        tamanho = (Integer) d.readObject();
    }

    public static void clearXML() throws IOException {
        Files.delete(Path.of("data.xml"));
    }

    public static Object getEmployeeAttribute(String employeeID, String attribute) throws Exception {

        Empregado employee = empregados.get(employeeID);

        return switch (attribute) {
            case "nome"             -> employee.getNome();
            case "endereco"         -> employee.getEndereco();
            case "tipo"             -> employee.getTipo();
            case "salario"          -> employee.getSalario();
            case "comissao"         -> ((EmpregadoComissionado) employee).getComissao();
            case "sindicalizado"    -> employee.getSindicalizado();
            case "metodoPagamento"  -> employee.getMetodo();
            case "banco"            -> ((Banco) employee.getMetodoPagamento()).getBanco();
            case "agencia"          -> ((Banco) employee.getMetodoPagamento()).getAgencia();
            case "contaCorrente"    -> ((Banco) employee.getMetodoPagamento()).getContaCorrente();
            case "idSindicato"      -> employee.membroSindicado.getIdMembro();
            case "taxaSindical"     -> employee.membroSindicado.getTaxaSindical();

            default -> throw new Exception("Atributo nao existe.");

        };

    }

    public static String getEmployeeByName(String name, int index) throws Exception {
        int i = 1;
        for (Map.Entry<String, Empregado> entry : Sistema.empregados.entrySet()) {
            Empregado empregado = entry.getValue();

            if (name.contains(empregado.getNome())) {
                if (i == index)
                    return entry.getKey();
                else
                    i++;
            }
        }
        throw new Exception("Nao ha empregado com esse nome.");
    }

    public static LocalDate getLocalDate(String date) throws Exception {

        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yyyy"));
        } catch (DateTimeParseException e) {
            throw new Exception("Data invalida.");
        }

    }

    public static String getWorkedHours(String employeeID, LocalDate initialDate, LocalDate finalDate) throws Exception {

        if (initialDate.isAfter(finalDate))
            throw new Exception("Data inicial nao pode ser posterior aa data final.");

        Empregado employee = empregados.get(employeeID);
        double horasAcumuladas = 0.0;
        for (CartaoDePonto card : ((EmpregadoHorista) employee).cartao) {
            if ((card.getData().isAfter(initialDate) || card.getData().isEqual(initialDate)) && card.getData().isBefore(finalDate)) {
                if (card.getHoras() > 8)
                    horasAcumuladas += 8.0;
                else
                    horasAcumuladas += card.getHoras();
            }
        }
        return Integer.toString((int) horasAcumuladas);
    }

    public static String getExtraWorkedHours(String employeeID, LocalDate initialDate, LocalDate finalDate) throws Exception {

        if (initialDate.isAfter(finalDate))
            throw new Exception("Data inicial nao pode ser posterior aa data final.");

        Empregado employee = empregados.get(employeeID);

        double horasAcumuladas = 0.0;
        for (CartaoDePonto card : ((EmpregadoHorista) employee).cartao) {
            if ((card.getData().isAfter(initialDate) || card.getData().isEqual(initialDate)) && card.getData().isBefore(finalDate)) {
                if (card.getHoras() > 8)
                    horasAcumuladas += (card.getHoras() - 8);
            }
        }
        if((int) horasAcumuladas == horasAcumuladas) return Integer.toString((int) horasAcumuladas);
        return Double.toString(horasAcumuladas).replace(".", ",");
    }

    public static String getSales(String employeeID, LocalDate initialDate, LocalDate finalDate) throws Exception {

        if (initialDate.isAfter(finalDate))
            throw new Exception("Data inicial nao pode ser posterior aa data final.");

        Empregado employee = empregados.get(employeeID);

        double valorTotal = 0.0;
        for(ResultadoDeVenda venda :((EmpregadoComissionado) employee).vendas) {
            if ((venda.getData().isAfter(initialDate) || venda.getData().isEqual(initialDate)) && venda.getData().isBefore(finalDate))
                valorTotal += venda.getValor();
        }

        return String.format("%.2f", valorTotal).replace(".", ",");
    }

    public static String getTotalPriceForTaxes(String employeeID, LocalDate initialDate, LocalDate finalDate) throws Exception {

        if (initialDate.isAfter(finalDate))
            throw new Exception("Data inicial nao pode ser posterior aa data final.");

        Empregado employee = empregados.get(employeeID);

        double taxaTotal = 0.0;
        for(TaxaServico venda : employee.membroSindicado.taxa) {
            if ((venda.getData().isAfter(initialDate) || venda.getData().isEqual(initialDate)) && venda.getData().isBefore(finalDate)) {
                taxaTotal += venda.getValor();
            }
        }

        return String.format("%.2f", taxaTotal).replace(".", ",");
    }

    public static void changeAttribute(String employeeID, String attribute, String value) throws Exception {

        Empregado employee = Sistema.empregados.get(employeeID);

        switch (attribute) {
            case "nome" -> employee.setNome(value);
            case "endereco" -> employee.setEndereco(value);
            case "tipo" -> {
                if (value.equals("horista"))
                    changeEmployeeTypeToHorista(employeeID);
                if (value.equals("comissionado"))
                    changeEmployeeTypeToComissionado(employeeID);
                if (value.equals("assalariado"))
                    changeEmployeeTypeToAssalariado(employeeID);
            }
            case "salario" -> {
                if (employee instanceof EmpregadoAssalariado)
                    ((EmpregadoAssalariado) employee).setSalarioMensal(value);
                else if (employee instanceof EmpregadoHorista)
                    ((EmpregadoHorista) employee).setSalarioPorHora(value);
                else if (employee instanceof EmpregadoComissionado)
                    ((EmpregadoComissionado) employee).setSalarioMensal(value);
            }
            case "sindicalizado" -> {
                if (!Boolean.valueOf(value))
                    employee.membroSindicado = null;
            }
            case "comissao" -> ((EmpregadoComissionado) employee).setComissao(value);
            case "metodoPagamento" -> {
                MetodoPagamento novoMetodo = employee.getMetodoPagamento();
                if (value.equals("correios"))
                    novoMetodo = new Correios();
                if (value.equals("emMaos"))
                    novoMetodo = new EmMaos();

                employee.setMetodoPagamento(novoMetodo);
            }
            default -> throw new Exception("Atributo nao existe.");
        }
    }

    public static boolean doesThisIDExists(String idSindicato) {
        for(Map.Entry<String, Empregado> entry: Sistema.empregados.entrySet()) {
            Empregado empregado = entry.getValue();
            if (empregado.getSindicalizado()) {
                if (empregado.membroSindicado.getIdMembro().equals(idSindicato))
                    return true;
            }
        }
        return false;
    }


















    public static void changeEmployeeTypeToHorista(String employeeID, String salary) throws Exception {

        Empregado employee = empregados.get(employeeID);

        employee = new EmpregadoHorista(employee.getNome(), employee.getEndereco(), salary);

        empregados.remove(employeeID);
        empregados.put(employeeID, employee);
    }

    public static void changeEmployeeTypeToHorista(String employeeID) throws Exception {

        Empregado employee = empregados.get(employeeID);

        employee = new EmpregadoHorista(employee.getNome(), employee.getEndereco(), null);

        empregados.remove(employeeID);
        empregados.put(employeeID, employee);
    }

    public static void changeEmployeeTypeToAssalariado(String employeeID, String salary) throws Exception {

        Empregado employee = empregados.get(employeeID);

        employee = new EmpregadoAssalariado(employee.getNome(), employee.getEndereco(), salary);

        empregados.remove(employeeID);
        empregados.put(employeeID, employee);
    }

    public static void changeEmployeeTypeToAssalariado(String employeeID) throws Exception {

        Empregado employee = empregados.get(employeeID);

        employee = new EmpregadoAssalariado(employee.getNome(), employee.getEndereco(), null);

        empregados.remove(employeeID);
        empregados.put(employeeID, employee);
    }

    public static void changeEmployeeTypeToComissionado(String employeeID, String comissao) throws Exception {

        Empregado employee = empregados.get(employeeID);

        employee = new EmpregadoComissionado(employee.getNome(), employee.getEndereco(), employee.getSalario(), comissao);

        empregados.remove(employeeID);
        empregados.put(employeeID, employee);
    }

    public static void changeEmployeeTypeToComissionado(String employeeID) throws Exception {

        Empregado employee = empregados.get(employeeID);

        employee = new EmpregadoComissionado(employee.getNome(), employee.getEndereco(), employee.getSalario(), null);

        empregados.remove(employeeID);
        empregados.put(employeeID, employee);
    }









}
