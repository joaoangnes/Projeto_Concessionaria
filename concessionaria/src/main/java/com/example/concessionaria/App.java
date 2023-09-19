package com.example.concessionaria;

import java.math.BigDecimal;

public class App {
    public static void main(String[] args) {
        
        BancoDeDadosArmazenamento bd_mongo = new BancoDeDadosArmazenamento();
        ArquivoArmazenamento arquivo = new ArquivoArmazenamento();

        Carro c1 = new Carro();
        c1.placa = "AZY2312";
        c1.ano_de_fabricacao = "2020";
        c1.marca = "Nissan";
        c1.modelo = "March";
        c1.numero_de_portas = 4;
        c1.preco = new BigDecimal("40400");

        Carro c2 = new Carro();
        c2.placa = "BFY2W327";
        c2.ano_de_fabricacao = "2023";
        c2.marca = "BMW";
        c2.modelo = "M3";
        c2.numero_de_portas = 4;
        c2.preco = new BigDecimal("800000");

        Motocicleta m1 = new Motocicleta();
        m1.placa = "MFY2F12";
        m1.ano_de_fabricacao = "2023";
        m1.marca = "Honda";
        m1.cilindradas = 1000;
        m1.modelo = "CBR";
        m1.preco = new BigDecimal("60500");

        Motocicleta m2 = new Motocicleta();
        m2.placa = "AFH8K79";
        m2.ano_de_fabricacao = "2023";
        m2.marca = "Yamaha";
        m2.cilindradas = 998;
        m2.modelo = "YSF R1";
        m2.preco = new BigDecimal("125990");


        Concessionaria conc_banco = new Concessionaria(bd_mongo);
        System.out.println("\n============= BANCO DE DADOS ==============");
        System.out.println("\n[Banco de Dados] Adicionando um novo carro: ");
        conc_banco.AdicionaVeiculoEstoque(c1);
        System.out.println("\n[Banco de Dados] Adicionando uma nova moto: ");
        conc_banco.AdicionaVeiculoEstoque(m1);
         System.out.println("\n[Banco de Dados] Adicionando um novo carro: ");
        conc_banco.AdicionaVeiculoEstoque(c2);
        System.out.println("\n[Banco de Dados] Adicionando uma nova moto: ");
        conc_banco.AdicionaVeiculoEstoque(m2);
        System.out.println("\n[Banco de Dados] Procurando veiculo com a placa (AZY2312)");
        conc_banco.EncontraVeiculoEstoque("AZY2312");
        System.out.println("\n[Banco de Dados] Procurando veiculo com a placa (AFH8K79)");
        conc_banco.EncontraVeiculoEstoque("AFH8K79");
        System.out.println("\n[Banco de Dados] Procurando veiculo com a placa (HZT2512)");
        conc_banco.EncontraVeiculoEstoque("HZT2512");
        System.out.println("\n[Banco de Dados] Listando todos os veiculos do estoque: ");
        conc_banco.ListarTodosVeiculos();

        System.out.println("\n====================================");
        System.out.println("\n============== ARQUIVO =============");
        Concessionaria conc_arquivo = new Concessionaria(arquivo);
        System.out.println("\n[Arquivo xls] Adicionando um novo carro: ");
        conc_arquivo.AdicionaVeiculoEstoque(c1);
        System.out.println("\n[Arquivo xls] Adicionando um novo carro: ");
        conc_arquivo.AdicionaVeiculoEstoque(c1);
        System.out.println("\n[Arquivo xls] Adicionando uma nova moto: ");
        conc_arquivo.AdicionaVeiculoEstoque(m2);
        System.out.println("\n[Arquivo xls] Adicionando um novo carro: ");
        conc_arquivo.AdicionaVeiculoEstoque(c2);
        System.out.println("\n[Arquivo xls] Adicionando uma nova moto: ");
        conc_arquivo.AdicionaVeiculoEstoque(m1);
        System.out.println("\n[Arquivo xls] Buscando veiculo placa (AFH8K79): ");
        conc_arquivo.EncontraVeiculoEstoque("AFH8K79");
        System.out.println("\n[Arquivo xls] Buscando veiculo placa (MFY2F12): ");
        conc_arquivo.EncontraVeiculoEstoque("MFY2F12");
        System.out.println("\n[Arquivo xls] Buscando veiculo placa (KAB2312): ");
        conc_arquivo.EncontraVeiculoEstoque("KAB2312");
        System.out.println("\n[Arquivo xls] Listando todos os veiculos do estoque: ");
        conc_arquivo.ListarTodosVeiculos();
    }
}
