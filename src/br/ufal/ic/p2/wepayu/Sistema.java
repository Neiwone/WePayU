package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.InputCheck;
import br.ufal.ic.p2.wepayu.models.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class Sistema {
    public static HashMap<String, Empregado> empregados;

    public static Integer tamanho;

    public Sistema() {
        empregados = new HashMap<String, Empregado>();
        tamanho = 0;
    }


    public static Object getEmployeeAttribute(String employeeID, String attribute) throws Exception {

        Empregado employee = empregados.get(employeeID);

        return switch (attribute) {
            case "nome" -> employee.getNome();
            case "endereco" -> employee.getEndereco();
            case "tipo" -> employee.getTipo();
            case "salario" -> employee.getSalario();
            case "comissao" -> ((EmpregadoComissionado) employee).getComissao();
            case "sindicalizado" -> employee.getSindicalizado();
            case "metodoPagamento" -> employee.getMetodo();
            case "banco" -> ((Banco) employee.getMetodoPagamento()).getBanco();
            case "agencia" -> ((Banco) employee.getMetodoPagamento()).getAgencia();
            case "contaCorrente" -> ((Banco) employee.getMetodoPagamento()).getContaCorrente();
            case "idSindicato" -> employee.membroSindicado.getIdMembro();
            case "taxaSindical" -> employee.membroSindicado.getTaxaSindical();

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
