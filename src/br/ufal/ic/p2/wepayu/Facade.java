package br.ufal.ic.p2.wepayu;

/*
     authors: Neiwone & rafaellucian0 on GitHub
 */

import br.ufal.ic.p2.wepayu.Exception.InputCheck;
import br.ufal.ic.p2.wepayu.models.*;

import java.io.IOException;
import java.time.LocalDate;

public class Facade {

    private Sistema sistema;
    public Facade() throws IOException {
        sistema = new Sistema();
    }



    /**
     * Method responsible for creating a new employee in our system.
     * @param nome employee's name;
     * @param endereco employee's address;
     * @param tipo kind of employee;
     * @param salario employee's salary;
     * @return employee's ID;
     * @throws Exception in case of any incorrect parameters.
     * @see Facade#criarEmpregado(String, String, String, String, String)
     */
    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.createEmployee(nome, endereco, tipo, salario);

        //  Retorna o id do novo empregado adicionado.
        return sistema.createNewEmployee(nome, endereco, tipo, salario);
    }



    /**
     * Method responsible for creating a new employee in our system.
     * (The difference about this method and {@link Facade#criarEmpregado(String, String, String, String) criarEmpregado} is that here we create a commissioned employee with commission).
     * @param nome employee's name;
     * @param endereco employee's address;
     * @param tipo kind of employee;
     * @param salario employee's salary;
     * @param comissao employee's commission;
     * @return employee's ID;
     * @throws Exception in case of any incorrect parameters.
     * @see Facade#criarEmpregado(String, String, String, String)
     */
    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.createEmployee(nome, endereco, tipo, salario, comissao);

        //  Retorna o id do novo empregado adicionado.
        return sistema.createNewEmployee(nome, endereco, tipo, salario, comissao);
    }



    /**
     * Method responsible for returning an employee's attribute.
     * @param emp employee's ID;
     * @param atributo attribute;
     * @return employee's attribute;
     * @throws Exception in case of any incorrect parameters.
     */
    public Object getAtributoEmpregado(String emp, String atributo) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.getEmployeeAttribute(emp, atributo);

        //  Retornando o valor do atributo do empregado.
        return sistema.getEmployeeAttribute(emp, atributo);

    }



    /**
     * Method responsible for returning the employee's ID that contains the required name.
     * @param nome employee's name;
     * @param indice which employee (first one, second one, ...);
     * @return employee's ID;
     * @throws Exception in case of any incorrect parameters.
     */
    public String getEmpregadoPorNome(String nome, int indice) throws Exception {
        //  Retorna a chave do empregado com o nome informado nos parametros.
        //  ( Caso haja dois ou mais empregados com o mesmo nome, o parametros
        //  indice é responsável em dizer qual é o certo).
        return sistema.getEmployeeByName(nome, indice);
    }



    /**
     * Method responsible for removing an employee.
     * @param emp employee's ID;
     * @throws Exception in case of invalid employee's ID.
     */
    public void removerEmpregado(String emp) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.removeEmployee(emp);

        //  Remove o empregado indentificado pela chave "emp".
        sistema.removeEmployee(emp);
    }



    /**
     * Method responsible for adding a new timecard to an {@link Empregado employee}.
     * @param emp employee's ID;
     * @param data date of timecard;
     * @param horas time of timecard;
     * @throws Exception in case of any invalid parameters.
     */
    public void lancaCartao(String emp, String data, String horas) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.addTimecard(emp, data, horas);

        //  Cria novo cartão de ponto com os parametros do metodo.
        CartaoDePonto novoPonto = new CartaoDePonto(
                data,
                Double.parseDouble(horas.replace(",", ".")));


        //  Adiciona novo cartão de ponto no ArrayList do empregado.
        sistema.addNewPointCard(emp, novoPonto);

    }



    /**
     * Method responsible for returning worked hours by an employee at a date gap.
     * @param emp employee's ID;
     * @param dataInicial initial date;
     * @param dataFinal final date;
     * @return the worked hours;
     * @throws Exception in case of any invalid parameters.
     */
    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.getWorkedHours(emp, dataInicial, dataFinal);

        //  Define a data inicial e a data final.
        LocalDate initialDate = Sistema.getLocalDate(dataInicial);
        LocalDate finalDate = Sistema.getLocalDate(dataFinal);

        //  Retorna as horas trabalhadas naquele intervalo de tempo.
        return sistema.getWorkedHours(emp, initialDate, finalDate);

    }



    /**
     * Method responsible for returning extra worked hours by an employee at a date gap.
     * @param emp employee's ID;
     * @param dataInicial initial date;
     * @param dataFinal final date;
     * @return the extra worked hours;
     * @throws Exception in case of any invalid parameters.
     */
    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal) throws Exception {

        // Checa os parametros para possiveis erros.
        InputCheck.getWorkedHours(emp, dataInicial, dataFinal);

        //  Define a data inicial e a data final.
        LocalDate initialDate = Sistema.getLocalDate(dataInicial);
        LocalDate finalDate = Sistema.getLocalDate(dataFinal);

        //  Retorna as horas extras trabalhadas naquele intervalo de tempo.
        return sistema.getExtraWorkedHours(emp, initialDate, finalDate);
    }



    /**
     * Method responsible for adding a new sales record to an {@link Empregado employee}.
     * @param emp employee's ID;
     * @param data date of the sales;
     * @param valor value of the saler;
     * @throws Exception in case of any invalid parameters.
     */
    public void lancaVenda(String emp, String data, String valor) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.addSaleRecord(emp, data, valor);

        //  Cria o novo Registro de venda.
        ResultadoDeVenda novaVenda =  new ResultadoDeVenda(
                data,
                Double.parseDouble(valor.replace(",", "."))
        );

        //  Adiciona o novo Registro na lista de vendas do empregado indentificado pela chave "emp".
        sistema.addNewSale(emp, novaVenda);

    }



    /**
     * Method responsible for returning the sales of an {@link Empregado employee}.
     * @param emp employee's ID;
     * @param dataInicial initial date;
     * @param dataFinal final date;
     * @throws Exception in case of any invalid parameters.
     */
    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal) throws Exception {

        //  Checa os parametros para possiveis erros.
        InputCheck.getSales(emp, dataInicial, dataFinal);

        //  Define a data inicial e a data final.
        LocalDate initialDate = Sistema.getLocalDate(dataInicial);
        LocalDate finalDate = Sistema.getLocalDate(dataFinal);

        //  Retorna o valor total das vendas realizadas pelo empregado naquele intervalo de tempo.
        return sistema.getSales(emp, initialDate, finalDate);

    }



    /**
     * Method responsible for returning the total {@link MembroSindicado#taxa tax service} of a {@link MembroSindicado Syndicate Member}.
     * @param emp employee's ID;
     * @param dataInicial initial date;
     * @param dataFinal final date;
     * @return sum of taxes;
     * @throws Exception in case of any invalid parameters.
     */
    public String getTaxasServico (String emp, String dataInicial , String dataFinal) throws Exception {

        // Checa os parametros para possiveis erros.
        InputCheck.getTaxService(emp, dataInicial, dataFinal);

        //  Define a data inicial e a data final.
        LocalDate initialDate = Sistema.getLocalDate(dataInicial);
        LocalDate finalDate = Sistema.getLocalDate(dataFinal);

        //  Retorna o valor de todas as taxas daquele membro do sindicato naquele intervalo de tempo.
        return sistema.getTotalPriceForTaxes(emp, initialDate, finalDate);

   }



    /**
     * Method responsible for adding a new tax service to an {@link MembroSindicado Syndicate Member}
     * @param membro Syndicate Member's ID;
     * @param data date of the tax;
     * @param valor tax's value;
     * @throws Exception in case of any invalid parameters.
     */
    public void lancaTaxaServico (String membro, String data, String valor) throws Exception {

        // Checa os parametros para possiveis erros.
        InputCheck.addTaxService(membro, data, valor);

        // Adiciona nova taxa de serviço
        sistema.addServiceTax(membro, data, valor);

    }



    /**
     * Method responsible for changing any attribute of an employee.
     * @param emp employee's ID;
     * @param atributo attribute to be changed;
     * @param valor1 attribute value;
     * @throws Exception in case of any invalid parameters.
     * @see Facade#alteraEmpregado(String, String, String, String, String, String)
     * @see Facade#alteraEmpregado(String, String, String, String, String)
     * @see Facade#alteraEmpregado(String, String, String, String)
     */
    public void alteraEmpregado(String emp, String atributo, String valor1) throws Exception {

        // Checa os parametros para possiveis erros.
        InputCheck.changeEmployeeInfo(emp, atributo, valor1, null, null, null);

        // Muda o atributo do empregado para o valor1.
        sistema.changeAttribute(emp, atributo, valor1);
    }



    /**
     * Method responsible for changing any attribute of an employee.
     * (Most used to change {@link MembroSindicado Syndicate Member} info)
     * @param emp employee's ID;
     * @param atributo attribute to be changed;
     * @param valor1 attribute value;
     * @param idSindicato {@link MembroSindicado Syndicate Member's} ID;
     * @param taxaSindical {@link MembroSindicado Syndicate Member's} tax;
     * @throws Exception in case of any invalid parameters.
     * @see Facade#alteraEmpregado(String, String, String, String, String, String)
     * @see Facade#alteraEmpregado(String, String, String, String)
     * @see Facade#alteraEmpregado(String, String, String)
     */
    public void alteraEmpregado(String emp, String atributo, String valor1, String idSindicato, String taxaSindical) throws Exception {

        // Checa os parametros para possiveis erros.
        InputCheck.changeEmployeeInfo(emp, atributo, valor1, idSindicato, taxaSindical, null);

        // Muda as informações correspondentes a um MembroSindicato.
        sistema.changeEmployeeInfo(emp,atributo, valor1, idSindicato, taxaSindical);
    }



    /**
     * Method responsible for changing any attribute of an employee.
     * (Most used to change employee's {@link Banco bank} info)
     * @param emp employee's ID;
     * @param atributo attribute to be changed;
     * @param valor1 attribute value;
     * @param agencia bank agency;
     * @param banco bank's name;
     * @param contaCorrente account;
     * @throws Exception in case of any invalid parameters.
     * @see Facade#alteraEmpregado(String, String, String, String, String)
     * @see Facade#alteraEmpregado(String, String, String, String)
     * @see Facade#alteraEmpregado(String, String, String)
     */
    public void alteraEmpregado(String emp, String atributo, String valor1, String banco, String agencia, String contaCorrente) throws Exception {

        // Checa os parametros para possiveis erros.
        InputCheck.changeEmployeeInfo(emp, atributo, valor1, banco, agencia, contaCorrente);

        // Muda as informações correspondentes a o MetodoPagamento de um empregado.
        sistema.changeEmployeeInfo(emp, atributo, valor1, banco, agencia, contaCorrente);
    }



    /**
     * Method responsible for changing any attribute of an employee.
     * (Most used to change commission/salary value)
     * @param emp employee's ID;
     * @param atributo attribute to be changed;
     * @param valor attribute value;
     * @param comissao commission value;
     * @throws Exception in case of any invalid parameters.
     * @see Facade#alteraEmpregado(String, String, String, String, String, String)
     * @see Facade#alteraEmpregado(String, String, String, String, String)
     * @see Facade#alteraEmpregado(String, String, String)
     */
    public void alteraEmpregado(String emp, String atributo, String valor, String comissao) throws Exception {

        // Checa os parametros para possiveis erros.
        InputCheck.changeEmployeeInfo(emp, atributo, valor, comissao, null, null);

        // Muda o tipo do empregado.
        sistema.changeEmployeesType(emp, atributo, valor, comissao);
    }


    /**
     * Method responsible for returning payroll's total of a date.
     * @param data date;
     * @return total of that payroll;
     * @throws Exception in case of an invalid date.
     */
    public String totalFolha(String data) throws Exception {

        // Retorna o total de uma folha de pagamento na forma de String.
        return String.format("%.2f", sistema.generateTotalPayroll(data)).replace('.', ',');
    }

    /**
     * Method responsible for creating payroll in a .txt file.
     * @param data date;
     * @param saida file name;
     * @throws Exception in case of an invalid date.
     */
    public void rodaFolha(String data, String saida) throws Exception {

        // Gera <saida>.txt correspondente a folha de pagamento daquela data.
        sistema.generatePayroll(data, saida);
    }


    /**
     * Method that returns the current number of employees in the system.
     * @return number of employees.
     */
    public int getNumeroDeEmpregados() {

        // Retorna o número atual de empregados no sistema.
        return sistema.getNumberOfEmployees();
    }


    /**
     * Method that undoes a change on our database.
     * @throws Exception if there is no change to undo or the system is currently closed.
     */
    public void undo() throws Exception {
        // Checa se o sistema está fechado.
        // (sistema fica fechado após ser executado encerrarSistema())
        if (this.sistema == null)
            throw new Exception("Nao pode dar comandos depois de encerrarSistema.");

        // Desfaz uma acão.
        // (isto é, restaurar a HashMap dos empregados para o estado antes da ação)
        sistema.undo();
    }


    /**
     * Method that redoes a change that was undone on our database.
     * @throws Exception if there is no change to redo.
     */
    public void redo() throws Exception {
        // Resfaz uma acão.
        // (isto é, restaurar a HashMap dos empregados para o estado antes do "undo")
        sistema.redo();
    }


    /**
     * Method that adds a new Payday that the employees now can choose as their payday.
     * @param descricao payday;
     * @throws Exception in case of an invalid payday.
     */
    public void criarAgendaDePagamentos(String descricao) throws Exception {

        // Checa dos parametros uma agenda de pagamento invalida.
        InputCheck.addNewPayday(descricao, sistema.getListOfPaydays());

        // Adiciona a nova agenda de pagemento na lista de possíveis periodos de pagamento.
        sistema.addPayday(descricao);
    }


    /**
     * Method responsible for clearing the system, this means that all employees in our system are going to be erased.
     * (This method also deletes the data persistence).
     * @throws IOException in case the .xml file won't be found.
     */
    public void zerarSistema() throws IOException {
        // Limpa os dados temporarios do sistema.
        sistema.clear();
    }



    /**
     * Method responsible for ending the system, here we do the data persistence by saving all information in a .xml file.
     * @throws IOException in case the .xml file won't be found.
     */
    public void encerrarSistema() throws IOException {
        // Salva os dados temporarios do sistema num arquivo "data.xml".
        sistema.save();
        // Define o sistema como encerrado.
        this.sistema = null;
    }


}
