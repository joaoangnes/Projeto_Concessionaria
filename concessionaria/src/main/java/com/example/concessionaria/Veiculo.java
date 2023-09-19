/**
 * A classe abstrata `Veiculo` serve como uma classe base para representar veículos genéricos.
 * Ela define atributos comuns a todos os veículos, como placa, marca, modelo, ano de fabricação e preço,
 * além de métodos para obtenção do tipo e criação de um documento MongoDB representando o veículo.
 * As classes derivadas devem implementar o método `adicionaAtributosEspecificos` para adicionar
 * atributos específicos ao documento MongoDB.
 */
package com.example.concessionaria;

import java.math.BigDecimal;
import org.apache.poi.ss.usermodel.*;
import org.bson.Document;

public abstract class Veiculo{
    // Atributos comuns a todos os veículos
    public String     placa; // Adicionado o atributo para identificar os veículos no estoque
    public String     marca;
    public String     modelo;
    public String     ano_de_fabricacao;
    public BigDecimal preco;

    /**
     * Obtém o tipo do veículo, que é o nome simples da classe.
     * @return O tipo do veículo.
     */
    public String getTipo() {
        return this.getClass().getSimpleName();
    }

    /**
     * Converte o veículo em um documento MongoDB.
     * @return Um documento MongoDB representando o veículo.
     */
    public Document toDocument() {
        Document document = new Document("tipo", getTipo())
                .append("placa", this.placa)
                .append("marca", this.marca)
                .append("modelo", this.modelo)
                .append("ano_de_fabricacao", this.ano_de_fabricacao)
                .append("preco", this.preco);

        // Deixa as classes derivadas adicionar seus atributos específicos, se houverem
        adicionaAtributosEspecificos(document);

        return document;
    }

    /**
     * Cria o cabeçalho do arquivo Excel para este tipo de veículo.
     * @param sheet A planilha onde o cabeçalho será adicionado.
     */
    public void arquivoCabecalho(Sheet sheet) {
        Integer colNum = 0;

        // Crie um cabeçalho
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(colNum++).setCellValue("Placa");
        headerRow.createCell(colNum++).setCellValue("Modelo");
        headerRow.createCell(colNum++).setCellValue("Marca");
        headerRow.createCell(colNum++).setCellValue("Ano de Fabricação");
        headerRow.createCell(colNum++).setCellValue("Preço");

        // Deixa as classes derivadas adicionar seus atributos específicos de cabeçalho, se houverem
        adicionaAtributosEspecificosHeader(headerRow, colNum);
    }

    /**
     * Converte o veículo em uma linha de dados para o arquivo Excel.
     * @param sheet A planilha onde a linha de dados será adicionada.
     */
    public void toRow(Sheet sheet) {
        Integer colNum = 0;
        Integer rowNum = sheet.getLastRowNum() + 1;

        Row bodyRow = sheet.createRow(rowNum++);
        bodyRow.createCell(colNum++).setCellValue(this.placa);
        bodyRow.createCell(colNum++).setCellValue(this.modelo);
        bodyRow.createCell(colNum++).setCellValue(this.marca);
        bodyRow.createCell(colNum++).setCellValue(this.ano_de_fabricacao);
        bodyRow.createCell(colNum++).setCellValue(this.preco.doubleValue());

        // Deixa as classes derivadas adicionar seus atributos específicos de corpo, se houverem
        adicionaAtributosEspecificosBody(bodyRow, colNum);
    }

    /**
     * Método abstrato que permite que as classes derivadas adicionem atributos específicos
     * ao documento MongoDB.
     * @param document O documento ao qual os atributos específicos serão adicionados.
     */
    protected abstract void adicionaAtributosEspecificos(Document document);

    /**
     * Método abstrato para adicionar atributos específicos de cabeçalho ao arquivo Excel.
     * @param headerRow A linha do cabeçalho onde os atributos específicos serão adicionados.
     * @param colNum O número da coluna a partir do qual os atributos serão adicionados.
     */
    protected abstract void adicionaAtributosEspecificosHeader(Row headerRow, Integer colNum);

    /**
     * Método abstrato para adicionar atributos específicos de corpo ao arquivo Excel.
     * @param bodyRow A linha do corpo onde os atributos específicos serão adicionados.
     * @param colNum O número da coluna a partir do qual os atributos serão adicionados.
     */
    protected abstract void adicionaAtributosEspecificosBody(Row bodyRow, Integer colNum);
}
