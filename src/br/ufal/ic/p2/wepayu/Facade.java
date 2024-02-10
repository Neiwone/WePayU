package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.InputCheck;
import br.ufal.ic.p2.wepayu.models.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Facade {

    private final Sistema sistema;
    public Facade() throws IOException {
        sistema = new Sistema();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.createEmployee(nome, endereco, tipo, salario);

        //  Retorna o id do novo empregado adicionado.
        return sistema.createNewEmployee(nome, endereco, tipo, salario);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.createEmployee(nome, endereco, tipo, salario, comissao);

        //  Retorna o id do novo empregado adicionado.
        return sistema.createNewEmployee(nome, endereco, tipo, salario, comissao);
    }

    /**
     * Metodo getAtributoEmpregado: retorna o atributo desejado do empregado identificado pelo ID.
     *
     * @param emp ID do empregado
     * @param atributo Atributo
     * @return atributo do empregado indentificado pela chave "emp"
     * @throws Exception Erro sobre algum paramentro
     */
    public Object getAtributoEmpregado(String emp, String atributo) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.getEmployeeAttribute(emp, atributo);

        //  Retornando o valor do atributo do empregado.
        return sistema.getEmployeeAttribute(emp, atributo);

    }

    public String getEmpregadoPorNome(String nome, int indice) throws Exception {
        //  Retorna a chave do empregado com o nome informado nos parametros.
        //  ( Caso haja dois ou mais empregados com o mesmo nome, o parametros
        //  indice é responsável em dizer qual é o certo).
        return sistema.getEmployeeByName(nome, indice);
    }
    public void removerEmpregado(String emp) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.removeEmployee(emp);

        //  Remove o empregado indentificado pela chave "emp".
        sistema.removeEmployee(emp);
    }

    /**
     * O metodo lancaCartao adiciona um novo cartão de ponto na lista de cartão do empregado desejado.
     * @param emp
     * @param data
     * @param horas
     * @throws Exception
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
    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.getWorkedHours(emp, dataInicial, dataFinal);

        //  Define a data inicial e a data final.
        LocalDate initialDate = Sistema.getLocalDate(dataInicial);
        LocalDate finalDate = Sistema.getLocalDate(dataFinal);

        //  Retorna as horas trabalhadas naquele intervalo de tempo.
        return sistema.getWorkedHours(emp, initialDate, finalDate);

    }
    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal) throws Exception {

        // Check dos parametros para possiveis erros.
        InputCheck.getWorkedHours(emp, dataInicial, dataFinal);

        //  Define a data inicial e a data final.
        LocalDate initialDate = Sistema.getLocalDate(dataInicial);
        LocalDate finalDate = Sistema.getLocalDate(dataFinal);

        //  Retorna as horas extras trabalhadas naquele intervalo de tempo.
        return sistema.getExtraWorkedHours(emp, initialDate, finalDate);
    }
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
    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal) throws Exception {

        //  Check dos parametros para possiveis erros.
        InputCheck.getSales(emp, dataInicial, dataFinal);

        //  Define a data inicial e a data final.
        LocalDate initialDate = Sistema.getLocalDate(dataInicial);
        LocalDate finalDate = Sistema.getLocalDate(dataFinal);

        //  Retorna o valor total das vendas realizadas pelo empregado naquele intervalo de tempo.
        return sistema.getSales(emp, initialDate, finalDate);

    }
    public String getTaxasServico (String emp, String dataInicial , String dataFinal) throws Exception {

        // Check dos parametros para possiveis erros.
        InputCheck.getTaxService(emp, dataInicial, dataFinal);

        //  Define a data inicial e a data final.
        LocalDate initialDate = Sistema.getLocalDate(dataInicial);
        LocalDate finalDate = Sistema.getLocalDate(dataFinal);

        //  Retorna o valor de todas as taxas daquele membro do sindicato naquele intervalo de tempo.
        return sistema.getTotalPriceForTaxes(emp, initialDate, finalDate);

   }
    public void lancaTaxaServico (String membro, String data, String valor) throws Exception {

        // Check dos parametros para possiveis erros.
        InputCheck.addTaxService(membro, data, valor);

        sistema.addServiceTax(membro, data, valor);

    }
    public void alteraEmpregado(String emp, String atributo, String valor1) throws Exception {

        // Check dos parametros para possiveis erros.
        InputCheck.changeEmployeeInfo(emp, atributo, valor1, null, null, null);

        // Muda o atributo do empregado para o valor1.
        sistema.changeAttribute(emp, atributo, valor1);
    }
    public void alteraEmpregado(String emp, String atributo, String valor1, String idSindicato, String taxaSindical) throws Exception {

        InputCheck.changeEmployeeInfo(emp, atributo, valor1, idSindicato, taxaSindical, null);

        sistema.changeEmployeeInfo(emp,atributo, valor1, idSindicato, taxaSindical);
    }
    public void alteraEmpregado(String emp, String atributo, String valor1, String banco, String agencia, String contaCorrente) throws Exception {

        InputCheck.changeEmployeeInfo(emp, atributo, valor1, banco, agencia, contaCorrente);

        sistema.changeEmployeeInfo(emp,atributo, valor1, banco, agencia, contaCorrente);
    }
    public void alteraEmpregado(String emp, String atributo, String valor, String comissao) throws Exception {

        InputCheck.changeEmployeeInfo(emp, atributo, valor, comissao, null, null);

        sistema.changeEmployeesType(emp, atributo, valor, comissao);
    }


    public void totalFolha(String data) throws Exception {

    }

    public void rodaFolha(String data, String saida) {

    }



    public void zerarSistema() throws IOException {
        sistema.clear();
    }

    public void encerrarSistema() throws IOException {
        sistema.save();
    }


}
