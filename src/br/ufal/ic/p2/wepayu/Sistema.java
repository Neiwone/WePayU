package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.models.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

public class Sistema {

    private final Database instance;
    private final History history = new History();


    public Sistema() throws IOException {
        instance = Database.getInstance();
        history.push(new Memento(instance.getEmpregados()));
    }

    public void clear() {
        instance.clear();
        history.push(new Memento(instance.getEmpregados()));
    }

    public void save() throws IOException {
        instance.saveToXML();
    }

    public String createNewEmployee(String name, String address, String type ,String salary) throws Exception {

        //  Checa qual o tipo do novo empregado.
        if(type.equals("horista")) {
            //  Cria e adiciona o novo empregado do tipo horista.
            EmpregadoHorista novoEmpregadoHorista = new EmpregadoHorista(name, address, Double.parseDouble(salary.replace(',', '.')));
            instance.addEmpregado(novoEmpregadoHorista);
            history.push(new Memento(instance.getEmpregados()));
            return novoEmpregadoHorista.getId();
        }
        else if(type.equals("assalariado")) {
            //  Cria e adiciona o novo empregado do tipo assalariado.
            EmpregadoAssalariado novoEmpregadoAssalariado = new EmpregadoAssalariado(name, address, Double.parseDouble(salary.replace(',', '.')));
            instance.addEmpregado(novoEmpregadoAssalariado);
            history.push(new Memento(instance.getEmpregados()));
            return novoEmpregadoAssalariado.getId();
        }
        else
            throw new Exception("Tipo invalido.");
    }

    public String createNewEmployee(String name, String address, String type, String salary, String commission) {

        String id = UUID.randomUUID().toString();

        EmpregadoComissionado novoEmpregadoComissionado = new EmpregadoComissionado(id, name, address, Double.parseDouble(salary.replace(',', '.')), Double.parseDouble(commission.replace(',', '.')));
        instance.addEmpregado(novoEmpregadoComissionado);
        history.push(new Memento(instance.getEmpregados()));
        return novoEmpregadoComissionado.getId();

    }

    public Object getEmployeeAttribute(String employeeID, String attribute) throws Exception {

        Empregado employee = instance.getEmpregado(employeeID);

        return switch (attribute) {
            case "nome"             -> employee.getNome();
            case "endereco"         -> employee.getEndereco();
            case "tipo"             -> employee.getTipo();
            case "salario"          -> String.format("%.2f", employee.getSalario()).replace('.', ',');
            case "comissao"         -> String.format("%.2f", ((EmpregadoComissionado) employee).getComissao()).replace('.', ',');
            case "sindicalizado"    -> employee.getSindicalizado();
            case "metodoPagamento"  -> employee.getMetodo();
            case "banco"            -> ((Banco) employee.getMetodoPagamento()).getBanco();
            case "agencia"          -> ((Banco) employee.getMetodoPagamento()).getAgencia();
            case "contaCorrente"    -> ((Banco) employee.getMetodoPagamento()).getContaCorrente();
            case "idSindicato"      -> employee.getMembroSindicado().getIdMembro();
            case "taxaSindical"     -> String.format("%.2f", employee.getMembroSindicado().getTaxaSindical()).replace(".", ",");
            case "agendaPagamento"  -> employee.getAgendaPagamento().getPeriodoPagamento();

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
        history.push(new Memento(instance.getEmpregados()));
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
            case "salario" -> employee.setSalario(Double.valueOf(value.replace(',', '.')));

            case "sindicalizado" -> {
                if (!Boolean.parseBoolean(value))
                    employee.setMembroSindicado(null);
            }
            case "comissao" -> ((EmpregadoComissionado) employee).setComissao(Double.valueOf(value.replace(',', '.')));

            case "metodoPagamento" -> {
                MetodoPagamento novoMetodo = employee.getMetodoPagamento();
                if (value.equals("correios"))
                    novoMetodo = new Correios();
                if (value.equals("emMaos"))
                    novoMetodo = new EmMaos();

                employee.setMetodoPagamento(novoMetodo);
            }

            case "agendaPagamento" -> {
                for (String payday : instance.getListOfPaydays())
                    if (payday.equals(value)) {
                        employee.getAgendaPagamento().setPeriodoPagamento(value);
                        history.push(new Memento(instance.getEmpregados()));
                        return;
                    }
                throw new Exception("Agenda de pagamento nao esta disponivel");
            }
            default -> throw new Exception("Atributo nao existe.");
        }
        history.push(new Memento(instance.getEmpregados()));
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
        history.push(new Memento(instance.getEmpregados()));
    }

    public void addNewSale(String employeeID, ResultadoDeVenda venda) {
        ((EmpregadoComissionado) instance.getEmpregado(employeeID)).vendas.add(venda);
        history.push(new Memento(instance.getEmpregados()));
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
                    history.push(new Memento(instance.getEmpregados()));
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
                history.push(new Memento(instance.getEmpregados()));
            }
        }


    }

    public void changeEmployeeInfo(String employeeID, String atributo, String valor1, String banco, String agencia, String contaCorrente) {

        if(atributo.equals("metodoPagamento") && valor1.equals("banco")) {
            instance.getEmpregado(employeeID).setMetodoPagamento(new Banco(banco, agencia, contaCorrente));
            history.push(new Memento(instance.getEmpregados()));
        }

    }


    public void changeEmployeesType(String emp, String atributo, String valor, String comissao) throws Exception {
        if (atributo.equals("tipo")) {
            switch (valor) {
                case "horista" -> changeEmployeeTypeToHorista(emp, comissao);
                case "assalariado" -> changeEmployeeTypeToAssalariado(emp, comissao);
                case "comissionado" -> changeEmployeeTypeToComissionado(emp, comissao);
                default -> throw new Exception("tipo invalido.");
            }
            history.push(new Memento(instance.getEmpregados()));
        }
    }





    private void changeEmployeeTypeToHorista(String employeeID, String salary) {

        Empregado employee = instance.getEmpregado(employeeID);

        employee = new EmpregadoHorista(employee.getId(), employee.getNome(), employee.getEndereco(), Double.parseDouble(salary.replace(',', '.')));

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

        employee = new EmpregadoAssalariado(employee.getId(), employee.getNome(), employee.getEndereco(), Double.parseDouble(salary.replace(',', '.')));

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

        employee = new EmpregadoComissionado(employee.getId(), employee.getNome(), employee.getEndereco(), employee.getSalario(), Double.parseDouble(comissao.replace(',', '.')));

        removeEmployee(employeeID);
        instance.addEmpregado(employeeID, employee);
    }

    private void changeEmployeeTypeToComissionado(String employeeID) {

        Empregado employee = instance.getEmpregado(employeeID);

        employee = new EmpregadoComissionado(employee.getId(), employee.getNome(), employee.getEndereco(), employee.getSalario(), null);

        removeEmployee(employeeID);
        instance.addEmpregado(employeeID, employee);
    }


    public Double getRawSalary(Empregado employee, LocalDate localDate) throws Exception {
        switch (employee.getTipo()) {
            case "assalariado" -> { return Math.floor(employee.getSalario() * (12D/52D * employee.getAgendaPagamento().getWeeks()) * 100)/100D; }
            case "horista" -> {
                double workedHours = Double.parseDouble(getWorkedHours(employee.getId(), employee.getAgendaPagamento().getDaysSinceLastPay(localDate), localDate).replace(',', '.'));
                double extraWorkedHours = Double.parseDouble(getExtraWorkedHours(employee.getId(), employee.getAgendaPagamento().getDaysSinceLastPay(localDate), localDate).replace(',', '.'));
                return (workedHours + (extraWorkedHours * 1.5)) * employee.getSalario();
            }
            case "comissionado" -> {
                double sales = Double.parseDouble(getSales(employee.getId(), employee.getAgendaPagamento().getDaysSinceLastPay(localDate), localDate).replace(',', '.'));
                return (Math.floor(employee.getSalario() * 12D/52D * employee.getAgendaPagamento().getWeeks() * 100)/100D + Math.floor((sales * ((EmpregadoComissionado) employee).getComissao()) * 100)/100D);
            }
        }
        return 0.0;
    }


    public Double generateTotalPayroll(String date) throws Exception {
        Double totalToPay = 0.0;
        LocalDate localDate = getLocalDate(date);
        for(Map.Entry<String, Empregado> employees: instance.getEmpregados().entrySet()) {
            Empregado employee = employees.getValue();
            switch (employee.getTipo())
            {
                case "assalariado", "horista", "comissionado" -> {
                    if(employee.getAgendaPagamento().ehDia(localDate))
                        totalToPay += getRawSalary(employee, localDate);
                }
                case null, default -> throw new Exception("invalid type trying to generate totalFolha");
            }
        }
        return totalToPay;
    }



    public static boolean isInteger(double number) {
        return number == (int) number;
    }

    public static String formatNumber(double number) {
        if (isInteger(number)) {
            // Se for inteiro, formata sem casas decimais
            return new DecimalFormat("#").format(number);
        } else {
            // Se não for inteiro, formata com duas casas decimais
            return new DecimalFormat("#.##").format(number).replace('.',',');
        }
    }

    public void generatePayroll(String date, String file) throws Exception {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yyyy"));
        localDate.format(DateTimeFormatter.ofPattern("yyyy-M-d"));

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        writer.write("FOLHA DE PAGAMENTO DO DIA " + localDate);
        writer.newLine();
        writer.write("====================================\n");
        writer.newLine();

        writer.write(
                """
                        ===============================================================================================================================
                        ===================== HORISTAS ================================================================================================
                        ===============================================================================================================================
                        Nome                                 Horas Extra Salario Bruto Descontos Salario Liquido Metodo
                        ==================================== ===== ===== ============= ========= =============== ======================================
                        """
        );

        LocalDate initialDate = localDate.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
        LinkedHashMap<String, Empregado> sortedEmployees = instance.getEmpregados().entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.comparing(Empregado::getNome))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        List<Double> totalh = Arrays.asList(0D,0D,0D,0D,0D);
        for(Map.Entry<String, Empregado> employees: sortedEmployees.entrySet()) {
            Empregado employee = employees.getValue();
            if (employee.getTipo().equals("horista") && employee.getAgendaPagamento().ehDia(localDate)) {
                String nome = employee.getNome();
                String horastrabalhadas = getWorkedHours(employee.getId(), initialDate, localDate);
                totalh.set(0, totalh.get(0) + Double.parseDouble(horastrabalhadas.replace(',', '.')));
                String horasextrastrabalhadas = getExtraWorkedHours(employee.getId(), initialDate, localDate);
                totalh.set(1, totalh.get(1) + Double.parseDouble(horasextrastrabalhadas.replace(',', '.')));
                Double salariobruto = getRawSalary(employee, localDate);
                totalh.set(2, totalh.get(2) + salariobruto);
                Double descontos = 0D;
                if (employee.getSindicalizado())
                    descontos = (employee.getMembroSindicado().getTaxaSindical() * 7) + Double.parseDouble(getTotalPriceForTaxes(employee.getId(), initialDate, localDate).replace(',', '.'));
                descontos += ((EmpregadoHorista) employee).getAcumuladodescontos();

                String salarioliquido = "0,00";
                if (descontos < salariobruto) {
                    salarioliquido = String.format("%.2f", salariobruto - descontos).replace('.', ',');
                    ((EmpregadoHorista) employee).setAcumuladodescontos(0D);
                }
                else {
                    ((EmpregadoHorista) employee).setAcumuladodescontos(descontos);
                    descontos = 0D;
                }
                totalh.set(3, totalh.get(3) + descontos);
                totalh.set(4, totalh.get(4) + Double.parseDouble(salarioliquido.replace(',','.')));
                String metodo = getMetodoPagamentoToPayroll(employee);


                writer.write(String.format("%-36s %5s %5s %13s %9s %15s %s", nome, horastrabalhadas, horasextrastrabalhadas, String.format("%.2f", salariobruto).replace('.', ','), String.format("%.2f", descontos).replace('.', ','), salarioliquido, metodo));
                writer.newLine();
            }
        }

        writer.write(String.format("\n%-36s %5s %5s %13s %9s %15s\n", "TOTAL HORISTAS", formatNumber(totalh.get(0)), formatNumber(totalh.get(1)), String.format("%.2f", totalh.get(2)).replace('.',','), String.format("%.2f", totalh.get(3)).replace('.',','), String.format("%.2f", totalh.get(4)).replace('.',',')));
        writer.newLine();


        writer.write(
                """
                        ===============================================================================================================================
                        ===================== ASSALARIADOS ============================================================================================
                        ===============================================================================================================================
                        Nome                                             Salario Bruto Descontos Salario Liquido Metodo\s
                        ================================================ ============= ========= =============== ======================================
                        """
        );

        List<Double> totala = Arrays.asList(0D,0D,0D);
        for(Map.Entry<String, Empregado> employees: sortedEmployees.entrySet()) {
            Empregado employee = employees.getValue();
            if (employee.getTipo().equals("assalariado") && localDate == localDate.with(TemporalAdjusters.lastDayOfMonth())) {
                String nome = employee.getNome();
                Double salariobruto = getRawSalary(employee, localDate);
                totala.set(0, totala.get(0) + salariobruto);
                Double descontos = 0D;
                if (employee.getSindicalizado())
                    descontos = (employee.getMembroSindicado().getTaxaSindical() * localDate.lengthOfMonth()) + Double.parseDouble(getTotalPriceForTaxes(employee.getId(), LocalDate.of(localDate.getYear(),localDate.getMonth(), 1), localDate).replace(',', '.'));
                totala.set(1, totala.get(1) + descontos);

                String salarioliquido = "0,00";
                if (descontos < salariobruto)
                    salarioliquido = String.format("%.2f", salariobruto - descontos).replace('.', ',');
                totala.set(2, totala.get(2) + Double.parseDouble(salarioliquido.replace(',','.')));
                String metodo = getMetodoPagamentoToPayroll(employee);


                writer.write(String.format("%-48s %13s %9s %15s %s", nome, String.format("%.2f", salariobruto).replace('.', ','), String.format("%.2f", descontos).replace('.', ','), salarioliquido, metodo));
                writer.newLine();
            }
        }

        writer.write(String.format("\n%-48s %13s %9s %15s\n", "TOTAL ASSALARIADOS", String.format("%.2f",totala.get(0)).replace('.', ','), String.format("%.2f",totala.get(1)).replace('.', ','), String.format("%.2f", totala.get(2)).replace('.',',')));
        writer.newLine();


        writer.write(
                """
                        ===============================================================================================================================
                        ===================== COMISSIONADOS ===========================================================================================
                        ===============================================================================================================================
                        Nome                  Fixo     Vendas   Comissao Salario Bruto Descontos Salario Liquido Metodo\s
                        ===================== ======== ======== ======== ============= ========= =============== ======================================
                        """
        );
        initialDate = localDate.minusDays(13);
        List<Double> totalc = Arrays.asList(0D,0D,0D,0D,0D,0D);
        for(Map.Entry<String, Empregado> employees: sortedEmployees.entrySet()) {
            Empregado employee = employees.getValue();
            if (employee.getTipo().equals("comissionado") && ((ChronoUnit.DAYS.between(LocalDate.of(2005,1,1), localDate)) + 1) % 14 == 0) {
                String nome = employee.getNome();
                Double salariofixo = Math.floor((employee.getSalario() * 24D/52D)*100)/100d;
                totalc.set(0, totalc.get(0) + salariofixo);
                String vendas = getSales(employee.getId(), initialDate, localDate);
                totalc.set(1, totalc.get(1) + Double.parseDouble(vendas.replace(',', '.')));
                Double comissao = Math.floor((((EmpregadoComissionado) employee).getComissao() * Double.parseDouble(vendas.replace(',', '.')))*100)/100D;
                totalc.set(2, totalc.get(2) + comissao);
                Double salariobruto = getRawSalary(employee, localDate);
                totalc.set(3, totalc.get(3) + salariobruto);
                Double descontos = 0D;
                if (employee.getSindicalizado())
                    descontos = (employee.getMembroSindicado().getTaxaSindical() * 14) + Double.parseDouble(getTotalPriceForTaxes(employee.getId(), initialDate, localDate).replace(',', '.'));
                totalc.set(4, totalc.get(4) + descontos);

                String salarioliquido = "0,00";
                if (descontos < salariobruto)
                    salarioliquido = String.format("%.2f", salariobruto - descontos).replace('.', ',');
                totalc.set(5, totalc.get(5) + Double.parseDouble(salarioliquido.replace(',','.')));
                String metodo = getMetodoPagamentoToPayroll(employee);


                writer.write(String.format("%-21s %8s %8s %8s %13s %9s %15s %s", nome, String.format("%.2f", salariofixo).replace('.', ','), vendas, String.format("%.2f", comissao).replace('.', ','), String.format("%.2f", salariobruto).replace('.', ','), String.format("%.2f", descontos).replace('.', ','), salarioliquido, metodo));
                writer.newLine();
            }
        }

        writer.write(String.format("\n%-21s %8s %8s %8s %13s %9s %15s\n", "TOTAL COMISSIONADOS", String.format("%.2f",totalc.get(0)).replace('.', ','), String.format("%.2f",totalc.get(1)).replace('.', ','), String.format("%.2f", totalc.get(2)).replace('.',','), String.format("%.2f", totalc.get(3)).replace('.',','), String.format("%.2f", totalc.get(4)).replace('.',','), String.format("%.2f", totalc.get(5)).replace('.',',')));
        writer.newLine();

        writer.write(String.format("TOTAL FOLHA: %.2f", generateTotalPayroll(date)).replace('.',','));
        writer.newLine();
        writer.close();
        history.push(new Memento(instance.getEmpregados()));
    }

    private String getMetodoPagamentoToPayroll(Empregado employee) {
        String metodo = "";
        MetodoPagamento pagamento = employee.getMetodoPagamento();
        if(Objects.equals(pagamento.getTipo(), "emMaos"))
            metodo = "Em maos";
        if(Objects.equals(pagamento.getTipo(), "banco"))
            metodo = String.format("%s, Ag. %s CC %s",
                    ((Banco) pagamento).getBanco(),
                    ((Banco) pagamento).getAgencia(),
                    ((Banco) pagamento).getContaCorrente()
            );
        if(Objects.equals(pagamento.getTipo(), "correios"))
            metodo = "Correios" + ", " + employee.getEndereco();
        return metodo;
    }

    public int getNumberOfEmployees() {
        return instance.getEmpregados().size();
    }

    public void undo() throws Exception {
        instance.setData(history.undo().restore());
    }

    public void redo() throws Exception {
        instance.setData(history.redo().restore());
    }

    public List<String> getListOfPaydays() {
        return instance.getListOfPaydays();
    }

    public void addPayday(String str) {
        instance.getListOfPaydays().add(str);
    }
}
