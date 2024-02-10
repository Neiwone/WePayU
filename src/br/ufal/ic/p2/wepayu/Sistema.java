package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.models.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Sistema {

    private Database instance;

    public Sistema() throws IOException {
        instance = Database.getInstance();;
    }

    public void clear() throws IOException {
        instance.clear();
    }

    public void save() throws IOException {
        instance.saveToXML();
    }

    public String createNewEmployee(String name, String address, String type ,String salary) throws Exception {

        //  Checa qual o tipo do novo empregado.
        if(type.equals("horista")) {
            //  Cria e adiciona o novo empregado do tipo horista.
            EmpregadoHorista novoEmpregadoHorista = new EmpregadoHorista(name, address, salary);
            instance.addEmpregado(novoEmpregadoHorista);
            return novoEmpregadoHorista.getId();
        }
        else if(type.equals("assalariado")) {
            //  Cria e adiciona o novo empregado do tipo assalariado.
            EmpregadoAssalariado novoEmpregadoAssalariado = new EmpregadoAssalariado(name, address, salary);
            instance.addEmpregado(novoEmpregadoAssalariado);
            return novoEmpregadoAssalariado.getId();
        }
        else
            throw new Exception("Tipo invalido.");

    }

    public String createNewEmployee(String name, String address, String type, String salary, String commission) throws Exception {

        String id = UUID.randomUUID().toString();

        EmpregadoComissionado novoEmpregadoComissionado = new EmpregadoComissionado(id, name, address, salary, commission);
        instance.addEmpregado(novoEmpregadoComissionado);
        return novoEmpregadoComissionado.getId();

    }

    public Object getEmployeeAttribute(String employeeID, String attribute) throws Exception {

        Empregado employee = instance.getEmpregado(employeeID);

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
            case "idSindicato"      -> employee.getMembroSindicado().getIdMembro();
            case "taxaSindical"     -> String.format("%.2f", employee.getMembroSindicado().getTaxaSindical()).replace(".", ",");

            default -> throw new Exception("Atributo nao existe.");

        };

    }

    public String getEmployeeByName(String name, int index) throws Exception {
        int i = 1;
        for (Map.Entry<String, Empregado> entry : instance.getEmpregados().entrySet()) {
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

    public void removeEmployee(String employeeID) {
        instance.removeEmpregado(employeeID);
    }

    public static LocalDate getLocalDate(String date) throws Exception {

        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yyyy"));
        } catch (DateTimeParseException e) {
            throw new Exception("Data invalida.");
        }

    }

    public String getWorkedHours(String employeeID, LocalDate initialDate, LocalDate finalDate) throws Exception {

        if (initialDate.isAfter(finalDate))
            throw new Exception("Data inicial nao pode ser posterior aa data final.");

        Empregado employee = instance.getEmpregado(employeeID);
        double horasAcumuladas = 0.0;
        for (CartaoDePonto card : ((EmpregadoHorista) employee).cartao) {
            LocalDate date = Sistema.getLocalDate(card.getData());
            if ((date.isAfter(initialDate) || date.isEqual(initialDate)) && date.isBefore(finalDate)) {
                if (card.getHoras() > 8)
                    horasAcumuladas += 8.0;
                else
                    horasAcumuladas += card.getHoras();
            }
        }
        return Integer.toString((int) horasAcumuladas);
    }

    public String getExtraWorkedHours(String employeeID, LocalDate initialDate, LocalDate finalDate) throws Exception {

        if (initialDate.isAfter(finalDate))
            throw new Exception("Data inicial nao pode ser posterior aa data final.");

        Empregado employee = instance.getEmpregado(employeeID);

        double horasAcumuladas = 0.0;
        for (CartaoDePonto card : ((EmpregadoHorista) employee).cartao) {
            LocalDate date = Sistema.getLocalDate(card.getData());
            if ((date.isAfter(initialDate) || date.isEqual(initialDate)) && date.isBefore(finalDate)) {
                if (card.getHoras() > 8)
                    horasAcumuladas += (card.getHoras() - 8);
            }
        }
        if((int) horasAcumuladas == horasAcumuladas) return Integer.toString((int) horasAcumuladas);
        return Double.toString(horasAcumuladas).replace(".", ",");
    }

    public String getSales(String employeeID, LocalDate initialDate, LocalDate finalDate) throws Exception {

        if (initialDate.isAfter(finalDate))
            throw new Exception("Data inicial nao pode ser posterior aa data final.");

        Empregado employee = instance.getEmpregado(employeeID);

        double valorTotal = 0.0;
        for(ResultadoDeVenda venda :((EmpregadoComissionado) employee).vendas) {
            LocalDate date = Sistema.getLocalDate(venda.getData());
            if ((date.isAfter(initialDate) || date.isEqual(initialDate)) && date.isBefore(finalDate))
                valorTotal += venda.getValor();
        }

        return String.format("%.2f", valorTotal).replace(".", ",");
    }

    public String getTotalPriceForTaxes(String employeeID, LocalDate initialDate, LocalDate finalDate) throws Exception {

        if (initialDate.isAfter(finalDate))
            throw new Exception("Data inicial nao pode ser posterior aa data final.");

        Empregado employee = instance.getEmpregado(employeeID);

        double taxaTotal = 0.0;
        for(TaxaServico venda : employee.getMembroSindicado().taxa) {
            LocalDate date =  Sistema.getLocalDate(venda.getData());
            if ((date.isAfter(initialDate) || date.isEqual(initialDate)) && date.isBefore(finalDate)) {
                taxaTotal += venda.getValor();
            }
        }

        return String.format("%.2f", taxaTotal).replace(".", ",");
    }

    public void changeAttribute(String employeeID, String attribute, String value) throws Exception {

        Empregado employee = instance.getEmpregado(employeeID);

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
                if (!Boolean.parseBoolean(value))
                    employee.setMembroSindicado(null);
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

    private boolean doesThisIDExists(String idSindicato) {
        for(Map.Entry<String, Empregado> entry: instance.getEmpregados().entrySet()) {
            Empregado empregado = entry.getValue();
            if (empregado.getSindicalizado()) {
                if (empregado.getMembroSindicado().getIdMembro().equals(idSindicato))
                    return true;
            }
        }
        return false;
    }

    public void addNewPointCard(String employeeID, CartaoDePonto cartao) {
        ((EmpregadoHorista) instance.getEmpregado(employeeID)).cartao.add(cartao);
    }

    public void addNewSale(String employeeID, ResultadoDeVenda venda) {
        ((EmpregadoComissionado) instance.getEmpregado(employeeID)).vendas.add(venda);
    }

    public void addServiceTax(String SyndicateID, String date, String value) throws Exception {
        for (Map.Entry<String, Empregado> entry: instance.getEmpregados().entrySet()) {
            Empregado empregado = entry.getValue();

            if (empregado.getMembroSindicado() != null) {
                if (Objects.equals(empregado.getMembroSindicado().idMembro, SyndicateID)) {
                    empregado.getMembroSindicado().taxa.add(new TaxaServico(
                            date,
                            Double.parseDouble(value.replace(",", ".")))
                    );
                    return;
                }
            }
        }

        throw new Exception("Membro nao existe.");
    }

    public void changeEmployeeInfo(String employeeID, String attribute, String value, String idSindicato, String taxaSindical) throws Exception {
        if(attribute.equals("sindicalizado") && value.equals("true")) {
            if (doesThisIDExists(idSindicato))
                throw new Exception("Ha outro empregado com esta identificacao de sindicato");
            else {
                instance.getEmpregado(employeeID).setMembroSindicado(new MembroSindicado());
                instance.getEmpregado(employeeID).getMembroSindicado().idMembro = idSindicato;
                instance.getEmpregado(employeeID).getMembroSindicado().taxaSindical = Double.parseDouble(taxaSindical. replace(",","."));
            }
        }


    }

    public void changeEmployeeInfo(String employeeID, String atributo, String valor1, String banco, String agencia, String contaCorrente) throws Exception {

        if(atributo.equals("metodoPagamento") && valor1.equals("banco"))
            instance.getEmpregado(employeeID).setMetodoPagamento(new Banco(banco, agencia, contaCorrente));

    }


    public void changeEmployeesType(String emp, String atributo, String valor, String comissao) throws Exception {
        if (atributo.equals("tipo")) {
            switch (valor) {
                case "horista" -> changeEmployeeTypeToHorista(emp, comissao);
                case "assalariado" -> changeEmployeeTypeToAssalariado(emp, comissao);
                case "comissionado" -> changeEmployeeTypeToComissionado(emp, comissao);
                default -> throw new Exception("tipo invalido.");
            }
        }
    }










    private void changeEmployeeTypeToHorista(String employeeID, String salary) {

        Empregado employee = instance.getEmpregado(employeeID);

        employee = new EmpregadoHorista(employee.getId(), employee.getNome(), employee.getEndereco(), salary);

        removeEmployee(employeeID);
        instance.addEmpregado(employeeID, employee);
    }

    private void changeEmployeeTypeToHorista(String employeeID) {

        Empregado employee = instance.getEmpregado(employeeID);

        employee = new EmpregadoHorista(employee.getId(), employee.getNome(), employee.getEndereco(), null);

        removeEmployee(employeeID);
        instance.addEmpregado(employeeID, employee);
    }

    private void changeEmployeeTypeToAssalariado(String employeeID, String salary) {

        Empregado employee = instance.getEmpregado(employeeID);

        employee = new EmpregadoAssalariado(employee.getId(), employee.getNome(), employee.getEndereco(), salary);

        removeEmployee(employeeID);
        instance.addEmpregado(employeeID, employee);
    }

    private void changeEmployeeTypeToAssalariado(String employeeID) {

        Empregado employee = instance.getEmpregado(employeeID);

        employee = new EmpregadoAssalariado(employee.getId(), employee.getNome(), employee.getEndereco(), null);

        removeEmployee(employeeID);
        instance.addEmpregado(employeeID, employee);
    }

    private void changeEmployeeTypeToComissionado(String employeeID, String comissao) {

        Empregado employee = instance.getEmpregado(employeeID);

        employee = new EmpregadoComissionado(employee.getId(), employee.getNome(), employee.getEndereco(), employee.getSalario(), comissao);

        removeEmployee(employeeID);
        instance.addEmpregado(employeeID, employee);
    }

    private void changeEmployeeTypeToComissionado(String employeeID) {

        Empregado employee = instance.getEmpregado(employeeID);

        employee = new EmpregadoComissionado(employee.getId(), employee.getNome(), employee.getEndereco(), employee.getSalario(), null);

        removeEmployee(employeeID);
        instance.addEmpregado(employeeID, employee);
    }









}
