/**
 * A interface `I_Armazenamento` define um contrato para operações de armazenamento e recuperação de informações
 * de veículos em um sistema de concessionária. Qualquer classe que implemente esta interface deve fornecer
 * implementações para os métodos declarados.
 */
package com.example.concessionaria;

import java.util.List;
import org.bson.Document;

public interface I_Armazenamento {
    /**
     * Adiciona um veículo ao estoque.
     *
     * @param veiculo O veículo a ser adicionado ao estoque.
     * @return `true` se a adição for bem-sucedida, `false` em caso de erro.
     */
    boolean addVeiculoEstoque(Veiculo veiculo);

    /**
     * Encontra um veículo no estoque com base no modelo.
     *
     * @param modelo O modelo do veículo a ser encontrado.
     * @return Um documento MongoDB representando o veículo encontrado ou `null` se não encontrado.
     */
    Document findVeiculoEstoque(String modelo);

    /**
     * Retorna uma lista de todos os veículos no estoque.
     *
     * @return Uma lista de documentos MongoDB representando os veículos no estoque.
     */
    List<Document> getAllVeiculos();
}
