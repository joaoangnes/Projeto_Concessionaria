/**
 * A classe `BancoDeDadosArmazenamento` implementa a interface `I_Armazenamento` e fornece métodos
 * para adicionar, buscar e recuperar informações de veículos no banco de dados MongoDB.
 */
package com.example.concessionaria;

import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.Document;

public class BancoDeDadosArmazenamento implements I_Armazenamento {
    // Conexão com o banco de dados MongoDB
    private final MongoDBConnection mongodb;

    // Nome da coleção de veículos no banco de dados
    private final String collectionName = "veiculos";

    /**
     * Construtor da classe `BancoDeDadosArmazenamento`.
     * Inicializa a conexão com o MongoDB através da classe `MongoDBConnection`.
     */
    public BancoDeDadosArmazenamento() {
        mongodb = MongoDBConnection.getInstance();
    }

    /**
     * Adiciona um veículo ao estoque no banco de dados MongoDB.
     *
     * @param veiculo O veículo a ser adicionado.
     * @return `true` se a inserção for bem-sucedida, `false` em caso de erro.
     */
    @Override
    public boolean addVeiculoEstoque(Veiculo veiculo) {
        try {
            // Obtém a coleção de veículos no banco de dados
            MongoCollection<Document> collection = this.mongodb.getCollection(collectionName);

            // Converte o veículo em um documento BSON
            Document document = veiculo.toDocument();

            // Insere o documento na coleção
            collection.insertOne(document);

            System.out.println("Veículo adicionado ao estoque: " + veiculo.modelo);
            return true; // Supondo que a inserção seja bem-sucedida
        } catch (MongoWriteException e) {
            if (e.getError().getCode() == 11000) {
                // Trata o erro de duplicação de placa
                System.out.println("[ERRO] Inserção: Já existe um veículo com a placa '" + veiculo.placa + "'.");
            } else {
                System.out.println("[ERRO] Inserção veículo ao estoque: " + e.getMessage());
            }
            return false;
        }
    }

    /**
     * Busca um veículo no estoque pelo número de placa.
     *
     * @param placa O número de placa do veículo a ser buscado.
     * @return Um documento MongoDB representando o veículo encontrado ou `null` se não encontrado.
     */
    @Override
    public Document findVeiculoEstoque(String placa) {
        try {
            // Obtém a coleção de veículos no banco de dados
            MongoCollection<Document> collection = this.mongodb.getCollection(collectionName);

            // Realiza uma consulta para encontrar o veículo com a placa especificada
            Document result = collection.find(Filters.eq("placa", placa)).first();
            return result;
        } catch (MongoWriteException e) {
            System.out.println("[ERRO] Busca veículo no estoque: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retorna uma lista de todos os veículos no estoque.
     *
     * @return Uma lista de documentos MongoDB representando os veículos no estoque.
     */
    @Override
    public List<Document> getAllVeiculos() {
        try {
            // Obtém a coleção de veículos no banco de dados
            MongoCollection<Document> collection = this.mongodb.getCollection(collectionName);

            // Realiza uma consulta para obter todos os veículos na coleção
            FindIterable<Document> veiculos = collection.find();
            List<Document> veiculosList = new ArrayList<>();

            // Converte os resultados em uma lista
            for (Document veiculoDoc : veiculos) {
                veiculosList.add(veiculoDoc);
            }

            return veiculosList;
        } catch (MongoWriteException e) {
            System.out.println("[ERRO] Busca veículos no estoque: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Fecha a conexão com o banco de dados MongoDB.
     */
    public void fecharConexao() {
        mongodb.closeConnection();
    }
}
