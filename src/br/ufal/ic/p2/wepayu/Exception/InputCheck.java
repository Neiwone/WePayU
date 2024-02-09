package br.ufal.ic.p2.wepayu.Exception;

// Class "InputCheck" to throw errors about facade-methods parameters.

import br.ufal.ic.p2.wepayu.Sistema;
import br.ufal.ic.p2.wepayu.models.Banco;
import br.ufal.ic.p2.wepayu.models.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.EmpregadoHorista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InputCheck extends Exception{

    private static void checkEmployeeID(String employeeID) throws Exception {
        if(employeeID.isEmpty())
            throw new Exception("Identificacao do empregado nao pode ser nula.");
        if(!Sistema.empregados.containsKey(employeeID))
            throw new Exception("Empregado nao existe.");
    }


    /**
     * Input check for createEmployee method without commission variable.
     *
     * @param name
     * @param adreess
     * @param type
     * @param salary
     * @throws Exception
     */

    public static void createEmployee(String name, String adreess, String type, String salary) throws Exception {
        if(name.isEmpty())
            throw new Exception("Nome nao pode ser nulo.");
        if(adreess.isEmpty())
            throw new Exception("Endereco nao pode ser nulo.");
        if(salary.isEmpty())
            throw new Exception("Salario nao pode ser nulo.");

        if(!salary.matches("[0-9,-]+"))
            throw new Exception("Salario deve ser numerico.");
        if(salary.contains("-"))
            throw new Exception("Salario deve ser nao-negativo.");

        if(type.equals("comissionado"))
            throw new Exception("Tipo nao aplicavel.");
    }


    /**
     * Input check for createEmployee method with commission variable.
     * @param name
     * @param adreess
     * @param type
     * @param salary
     * @param commission
     * @throws Exception
     */
    public static void createEmployee(String name, String adreess, String type, String salary, String commission) throws Exception {
        if(name.isEmpty())
            throw new Exception("Nome nao pode ser nulo.");
        if(adreess.isEmpty())
            throw new Exception("Endereco nao pode ser nulo.");
        if(salary.isEmpty())
            throw new Exception("Salario nao pode ser nulo.");
        if(commission.isEmpty())
            throw new Exception("Comissao nao pode ser nula.");

        if(!salary.matches("[0-9,-]+"))
            throw new Exception("Salario deve ser numerico.");
        if(salary.contains("-"))
            throw new Exception("Salario deve ser nao-negativo.");


        if(commission.contains("-"))
            throw new Exception("Comissao deve ser nao-negativa.");
        if(!commission.matches("[0-9,-]+"))
            throw new Exception("Comissao deve ser numerica.");

        if(!type.equals("comissionado"))
            throw new Exception("Tipo nao aplicavel.");
    }

    /**
     * Input check for getEmployeeAttribute method.
     * @param employeeID
     * @param attribute
     * @throws Exception
     */
    public static void getEmployeeAttribute(String employeeID, String attribute) throws Exception {

        checkEmployeeID(employeeID);

        if(attribute.equals("comissao"))
            if(!(Sistema.empregados.get(employeeID) instanceof EmpregadoComissionado))
                throw new Exception("Empregado nao eh comissionado.");

        if((attribute.equals("banco") || attribute.equals("agencia") || attribute.equals("contaCorrente")))
            if(!(Sistema.empregados.get(employeeID).getMetodoPagamento() instanceof Banco))
                throw new Exception("Empregado nao recebe em banco.");

        if((attribute.equals("idSindicato") || attribute.equals("taxaSindical")))
            if (!Sistema.empregados.get(employeeID).getSindicalizado())
                throw new Exception("Empregado nao eh sindicalizado.");
    }


    public static void removeEmployee(String employeeID) throws Exception {
        checkEmployeeID(employeeID);
    }


    public static void addTimecard(String employeeID, String hours) throws Exception {

        checkEmployeeID(employeeID);

        if(hours.contains("-") || Double.parseDouble(hours.replace(",", ".")) == 0)
            throw new Exception("Horas devem ser positivas.");

        if(!(Sistema.empregados.get(employeeID) instanceof EmpregadoHorista))
            throw new Exception("Empregado nao eh horista.");

    }

    public static void addSaleRecord(String employeeID, String value) throws Exception {

        checkEmployeeID(employeeID);

        if(value.contains("-") || Double.parseDouble(value.replace(",", ".")) == 0)
            throw new Exception("Valor deve ser positivo.");

        if(!(Sistema.empregados.get(employeeID) instanceof EmpregadoComissionado))
            throw new Exception("Empregado nao eh comissionado.");
    }


    public static void getWorkedHours(String employeeID, String initialDate, String finalDate) throws Exception {

        checkEmployeeID(employeeID);

        if(!(Sistema.empregados.get(employeeID) instanceof EmpregadoHorista))
            throw new Exception("Empregado nao eh horista.");

        int i = 0, day = 0, month = 0;
        for(String s : finalDate.split("/")) {
            if(i == 0) {
                day = Integer.valueOf(s);
                i++;
            }
            else if(i == 1) {
                month = Integer.valueOf(s);
                i++;
            }
        }
        if(day > 28 && month == 2)
            throw new Exception("Data final invalida.");
        if(day > 31 || month > 12)
            throw new Exception("Data final invalida.");

        i = 0;
        day = 0;
        month = 0;

        for(String s : initialDate.split("/")) {
            if(i == 0) {
                day = Integer.valueOf(s);
                i++;
            }
            else if(i == 1) {
                month = Integer.valueOf(s);
                i++;
            }
        }

        if(day > 28 && month == 2)
            throw new Exception("Data inicial invalida.");
        if(day > 31 || month > 12)
            throw new Exception("Data inicial invalida.");
    }

    public static void getSales(String employeeID, String initialDate, String finalDate) throws Exception {

        checkEmployeeID(employeeID);

        if(!(Sistema.empregados.get(employeeID) instanceof EmpregadoComissionado))
            throw new Exception("Empregado nao eh comissionado.");

        int i = 0, day = 0, month = 0;
        for(String s : finalDate.split("/")) {
            if(i == 0) {
                day = Integer.valueOf(s);
                i++;
            }
            else if(i == 1) {
                month = Integer.valueOf(s);
                i++;
            }
        }
        if(day > 28 && month == 2)
            throw new Exception("Data final invalida.");
        if(day > 31 || day <= 0)
            throw new Exception("Data final invalida.");

        i = 0;
        day = 0;
        month = 0;

        for(String s : initialDate.split("/")) {
            if(i == 0) {
                day = Integer.valueOf(s);
                i++;
            }
            else if(i == 1) {
                month = Integer.valueOf(s);
                i++;
            }
        }

        if(day > 28 && month == 2)
            throw new Exception("Data inicial invalida.");
        if(day > 31 || day <= 0)
            throw new Exception("Data inicial invalida.");
    }

    public static void getTaxService(String employeeID, String initialDate, String finalDate) throws Exception {

        checkEmployeeID(employeeID);

        if(!Sistema.empregados.get(employeeID).getSindicalizado())
            throw new Exception("Empregado nao eh sindicalizado.");

        int i = 0, day = 0, month = 0;
        for(String s : finalDate.split("/")) {
            if(i == 0) {
                day = Integer.valueOf(s);
                i++;
            }
            else if(i == 1) {
                month = Integer.valueOf(s);
                i++;
            }
        }
        if(day > 28 && month == 2)
            throw new Exception("Data final invalida.");

        i = 0;
        day = 0;
        month = 0;

        for(String s : initialDate.split("/")) {
            if(i == 0) {
                day = Integer.valueOf(s);
                i++;
            }
            else if(i == 1) {
                month = Integer.valueOf(s);
                i++;
            }
        }

        if(day > 28 && month == 2)
            throw new Exception("Data inicial invalida.");
    }


    public static void addTaxService(String SyndicateMemberID, String value) throws Exception {

        if (SyndicateMemberID.isEmpty())
            throw new Exception("Identificacao do membro nao pode ser nula.");

        if(value.contains("-") || Double.parseDouble(value.replace(",", ".")) == 0)
            throw new Exception("Valor deve ser positivo.");

    }


    public static void changeEmployeeInfo(String employeeID, String attribute, String value, String value1, String value2, String value3) throws Exception {

        checkEmployeeID(employeeID);

        if(attribute.equals("comissao"))
            if (!(Sistema.empregados.get(employeeID) instanceof EmpregadoComissionado))
                throw new Exception("Empregado nao eh comissionado.");

        switch (attribute) {
            case "nome" -> {
                if(value.isEmpty()) throw new Exception("Nome nao pode ser nulo.");
            }
            case "endereco" -> {
                if(value.isEmpty()) throw new Exception("Endereco nao pode ser nulo.");
            }
            case "tipo" -> {
                if(!(value.equals("assalariado") || value.equals("horista") || value.equals("comissionado"))) throw new Exception("Tipo invalido.");
            }
            case "salario" -> {
                if(value.isEmpty()) throw new Exception("Salario nao pode ser nulo.");
                if(value.contains("-")) throw new Exception("Salario deve ser nao-negativo.");
                if(!value.matches("[0-9,-]+")) throw new Exception("Salario deve ser numerico.");
            }
            case "comissao" -> {
                if(value.isEmpty()) throw new Exception("Comissao nao pode ser nula.");
                if(value.contains("-")) throw new Exception("Comissao deve ser nao-negativa.");
                if(!value.matches("[0-9,-]+")) throw new Exception("Comissao deve ser numerica.");
            }
            case "metodoPagamento" -> {
                if(value.isEmpty()) throw new Exception("Metodo de pagamento invalido.");
                if(!(value.equals("banco") || value.equals("emMaos") || value.equals("correios"))) throw new Exception("Metodo de pagamento invalido.");
                if(value1 != null && value1.isEmpty()) throw new Exception("Banco nao pode ser nulo.");
                if(value2 != null && value2.isEmpty()) throw new Exception("Agencia nao pode ser nulo.");
                if(value3 != null && value3.isEmpty()) throw new Exception("Conta corrente nao pode ser nulo.");
            }
            case "sindicalizado" -> {
                if(!(value.equals("true") || value.equals("false"))) throw new Exception("Valor deve ser true ou false.");
                if(value1 != null && value1.isEmpty()) throw new Exception("Identificacao do sindicato nao pode ser nula.");
                if(value2 != null && value2.isEmpty()) throw new Exception("Taxa sindical nao pode ser nula.");
                if(value2 != null && value2.contains("-")) throw new Exception("Taxa sindical deve ser nao-negativa.");
                if(value2 != null && !value2.matches("[0-9,-]+")) throw new Exception("Taxa sindical deve ser numerica.");

            }


        }
    }















}