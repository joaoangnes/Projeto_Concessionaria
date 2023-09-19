/**
 * A classe `Motocicleta` é uma subclasse de `Veiculo` e representa um tipo específico de veículo.
 * Ela adiciona o atributo `cilindradas` aos atributos herdados de `Veiculo` e implementa o método
 * `adicionaAtributosEspecificos` para adicionar atributos específicos de uma motocicleta ao documento MongoDB.
 */
package com.example.concessionaria;

import org.apache.poi.ss.usermodel.Row;
import org.bson.Document;

public class Motocicleta extends Veiculo {
    // Número de cilindradas da motocicleta
    public Integer cilindradas;

    /**
     * Método protegido para adicionar atributos específicos de uma motocicleta ao documento MongoDB.
     * @param document O documento ao qual os atributos serão adicionados.
     */
    @Override
    protected void adicionaAtributosEspecificos(Document document) {
        document.append("cilindradas", this.cilindradas);
    }

    /**
     * Método protegido para adicionar o cabeçalho específico de uma motocicleta ao arquivo Excel.
     * @param headerRow A linha do cabeçalho onde os atributos específicos serão adicionados.
     * @param colNum O número da coluna a partir do qual os atributos serão adicionados.
     */
    @Override
    protected void adicionaAtributosEspecificosHeader(Row headerRow, Integer colNum) {
        headerRow.createCell(colNum++).setCellValue("Cilindradas");
    }

    /**
     * Método protegido para adicionar os atributos específicos de uma motocicleta ao corpo do arquivo Excel.
     * @param bodyRow A linha do corpo onde os atributos específicos serão adicionados.
     * @param colNum O número da coluna a partir do qual os atributos serão adicionados.
     */
    @Override
    protected void adicionaAtributosEspecificosBody(Row bodyRow, Integer colNum) {
        bodyRow.createCell(colNum++).setCellValue(this.cilindradas);
    }
}
