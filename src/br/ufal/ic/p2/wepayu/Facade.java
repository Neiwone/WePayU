package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.InputCheck;
import br.ufal.ic.p2.wepayu.models.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Facade {

    public Facade() throws FileNotFoundException {
        try {
            Sistema.getFromXML();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
    }
    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.createEmployee(nome, endereco, tipo, salario);

        //  Checa qual o tipo do novo empregado.
        if(tipo.equals("horista"))
            //  Cria e adiciona o novo empregado do tipo horista.
            Sistema.empregados.put(String.valueOf(Sistema.tamanho), new EmpregadoHorista(nome, endereco, salario));
        else if(tipo.equals("assalariado"))
            //  Cria e adiciona o novo empregado do tipo assalariado.
            Sistema.empregados.put(String.valueOf(Sistema.tamanho), new EmpregadoAssalariado(nome, endereco, salario));
        else
            throw new Exception("Tipo invalido.");

        //  Retorna o id do novo empregado adicionado.
        return Integer.toString(Sistema.tamanho++);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.createEmployee(nome, endereco, tipo, salario, comissao);

        //  Cria e adiciona o novo empregado.
        Sistema.empregados.put(String.valueOf(Sistema.tamanho), new EmpregadoComissionado(nome, endereco, salario, comissao));

        //  Retorna o id do novo empregado adicionado.
        return Integer.toString(Sistema.tamanho++);
    }

    /**
     * Metodo getAtributoEmpregado: retorna o atributo desejado do empregado identificado pelo ID.
     *
     * @param emp
     * @param atributo
     * @return atributo do empregado indentificado pela chave "emp"
     * @throws Exception
     */
    public Object getAtributoEmpregado(String emp, String atributo) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.getEmployeeAttribute(emp, atributo);

        //  Retornando o valor do atributo do empregado.
        return Sistema.getEmployeeAttribute(emp, atributo);

    }

    public String getEmpregadoPorNome(String nome, int indice) throws Exception {
        //  Retorna a chave do empregado com o nome informado nos parametros.
        //  ( Caso haja dois ou mais empregados com o mesmo nome, o parametros
        //  indice é responsável em dizer qual é o certo).
        return Sistema.getEmployeeByName(nome, indice);
    }
    public void removerEmpregado(String emp) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.removeEmployee(emp);

        //  Remove o empregado indentificado pela chave "emp".
        Sistema.empregados.remove(emp);
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

        //  Salva instancia do empregado identificado pela chave "emp".
        Empregado empregado = Sistema.empregados.get(emp);

        //  Adiciona novo cartão de ponto no ArrayList do empregado.
        ((EmpregadoHorista) empregado).cartao.add(novoPonto);

    }
    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.getWorkedHours(emp, dataInicial, dataFinal);

        //  Define a data inicial e a data final.
        LocalDate initialDate = Sistema.getLocalDate(dataInicial);
        LocalDate finalDate = Sistema.getLocalDate(dataFinal);

        //  Retorna as horas trabalhadas naquele intervalo de tempo.
        return Sistema.getWorkedHours(emp, initialDate, finalDate);

    }
    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal) throws Exception {

        // Check dos parametros para possiveis erros.
        InputCheck.getWorkedHours(emp, dataInicial, dataFinal);

        //  Define a data inicial e a data final.
        LocalDate initialDate = Sistema.getLocalDate(dataInicial);
        LocalDate finalDate = Sistema.getLocalDate(dataFinal);

        //  Retorna as horas extras trabalhadas naquele intervalo de tempo.
        return Sistema.getExtraWorkedHours(emp, initialDate, finalDate);
    }
    public void lancaVenda(String emp, String data, String valor) throws Exception {

        //  Verifica os parametros para possiveis erros.
        InputCheck.addSaleRecord(emp, data, valor);

        //  Cria o novo Registro de venda.
        ResultadoDeVenda novaVenda =  new ResultadoDeVenda(
                data,
                Double.parseDouble(valor.replace(",", "."))
        );

        Empregado empregado = Sistema.empregados.get(emp);
        //  Adiciona o novo Registro na lista de vendas do empregado indentificado pela chave "emp".
        ((EmpregadoComissionado) empregado).vendas.add(novaVenda);

    }
    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal) throws Exception {

        //  Check dos parametros para possiveis erros.
        InputCheck.getSales(emp, dataInicial, dataFinal);

        //  Define a data inicial e a data final.
        LocalDate initialDate = Sistema.getLocalDate(dataInicial);
        LocalDate finalDate = Sistema.getLocalDate(dataFinal);

        //  Retorna o valor total das vendas realizadas pelo empregado naquele intervalo de tempo.
        return Sistema.getSales(emp, initialDate, finalDate);

    }
    public String getTaxasServico (String emp, String dataInicial , String dataFinal) throws Exception {

        // Check dos parametros para possiveis erros.
        InputCheck.getTaxService(emp, dataInicial, dataFinal);

        //  Define a data inicial e a data final.
        LocalDate initialDate = Sistema.getLocalDate(dataInicial);
        LocalDate finalDate = Sistema.getLocalDate(dataFinal);

        //  Retorna o valor de todas as taxas daquele membro do sindicato naquele intervalo de tempo.
        return Sistema.getTotalPriceForTaxes(emp, initialDate, finalDate);

   }
    public void lancaTaxaServico (String membro, String data, String valor) throws Exception {

        // Check dos parametros para possiveis erros.
        InputCheck.addTaxService(membro, data, valor);

        for (Map.Entry<String, Empregado> entry: Sistema.empregados.entrySet()) {
            Empregado empregado = entry.getValue();

            if (empregado.membroSindicado != null) {
                if (Objects.equals(empregado.membroSindicado.idMembro, membro)) {
                    empregado.membroSindicado.taxa.add(new TaxaServico(
                            data,
                            Double.parseDouble(valor.replace(",", ".")))
                    );
                    return;
                }
            }
        }

        throw new Exception("Membro nao existe.");

    }
    public void alteraEmpregado(String emp, String atributo, String valor1) throws Exception {

        // Check dos parametros para possiveis erros.
        InputCheck.changeEmployeeInfo(emp, atributo, valor1, null, null, null);

        // Muda o atributo do empregado para o valor1.
        Sistema.changeAttribute(emp, atributo, valor1);
    }
    public void alteraEmpregado(String emp, String atributo, String valor1, String idSindicato, String taxaSindical) throws Exception {

        InputCheck.changeEmployeeInfo(emp, atributo, valor1, idSindicato, taxaSindical, null);

        if(atributo.equals("sindicalizado") && valor1.equals("true")) {
            if (Sistema.doesThisIDExists(idSindicato))
                throw new Exception("Ha outro empregado com esta identificacao de sindicato");
            else {
                Sistema.empregados.get(emp).membroSindicado = new MembroSindicado();
                Sistema.empregados.get(emp).membroSindicado.idMembro = idSindicato;
                Sistema.empregados.get(emp).membroSindicado.taxaSindical = Double.valueOf(taxaSindical);
            }
        }
    }
    public void alteraEmpregado(String emp, String atributo, String valor1, String banco, String agencia, String contaCorrente) throws Exception {

        InputCheck.changeEmployeeInfo(emp, atributo, valor1, banco, agencia, contaCorrente);

        if(atributo.equals("metodoPagamento") && valor1.equals("banco"))
            Sistema.empregados.get(emp).setMetodoPagamento(new Banco(banco, agencia, contaCorrente));

    }
    public void alteraEmpregado(String emp, String atributo, String valor, String comissao) throws Exception {

        InputCheck.changeEmployeeInfo(emp, atributo, valor, comissao, null, null);

        if (atributo.equals("tipo")) {
            switch (valor) {
                case "horista" -> Sistema.changeEmployeeTypeToHorista(emp, comissao);
                case "assalariado" -> Sistema.changeEmployeeTypeToAssalariado(emp, comissao);
                case "comissionado" -> Sistema.changeEmployeeTypeToComissionado(emp, comissao);
                default -> throw new Exception("tipo invalido.");
            }
        }
    }

    public void zerarSistema() throws IOException {
        Sistema.clearXML();

        Sistema.empregados = new HashMap<>();
        Sistema.tamanho = 0;
    }

    public void encerrarSistema() throws IOException {
        Sistema.saveToXML();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Facade facade = new Facade();
        Sistema.empregados.forEach((id, empregado) -> System.out.println(empregado + " " + id));
    }

}
