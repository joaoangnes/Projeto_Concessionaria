/**
 * A classe `MongoDBConnection` representa uma conexão Singleton com um servidor MongoDB e oferece métodos
 * para acessar o banco de dados MongoDB e suas coleções.
 */
package com.example.concessionaria;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    // Nome do banco de dados MongoDB
    private static final String DATABASE_NAME = "concessionaria"; // Substitua pelo nome do seu banco de dados

    // URL de conexão do MongoDB
    private static final String MONGODB_URI = "mongodb+srv://joaoangnes:teste1234@clusterconcessionaria.os2re7q.mongodb.net/?retryWrites=true&w=majority";

    // Instância única da conexão MongoDB (Singleton)
    private static MongoDBConnection instance;

    private MongoClient mongoClient;
    private MongoDatabase database;

    /**
     * Construtor privado para criar uma instância única da conexão MongoDB (Singleton).
     */
    private MongoDBConnection() {
        try {
            // Cria um cliente MongoDB com a URL de conexão especificada
            mongoClient = MongoClients.create(MONGODB_URI);
            // Obtém o banco de dados especificado
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Conexão com MongoDB estabelecida com sucesso.");
        } catch (Exception e) {
            System.out.println("Falha na conexão com o MongoDB: " + e.getMessage());
        }
    }

    /**
     * Método para obter a instância única da conexão MongoDB.
     * Utiliza o padrão Singleton para garantir uma única instância da conexão.
     *
     * @return A instância única da conexão MongoDB.
     */
    public static MongoDBConnection getInstance() {
        if (instance == null) {
            synchronized (MongoDBConnection.class) {
                if (instance == null) {
                    instance = new MongoDBConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Método para obter o banco de dados MongoDB.
     *
     * @return O banco de dados MongoDB.
     */
    public MongoDatabase getDatabase() {
        return database;
    }

    /**
     * Método para obter a coleção de dados MongoDB com o nome especificado.
     *
     * @param collectionName O nome da coleção.
     * @return A coleção de dados MongoDB.
     */
    public MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    /**
     * Método para fechar a conexão com o MongoDB.
     */
    public void closeConnection() {
        try {
            mongoClient.close();
            System.out.println("Conexão com MongoDB fechada com sucesso.");
        } catch (Exception e) {
            System.out.println("[ERRO] - Falha ao fechar a conexão com o MongoDB: " + e.getMessage());
        }
    }
}
