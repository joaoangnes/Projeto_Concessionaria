/**
 * A classe `Concessionaria` representa uma concessionária de veículos e fornece métodos para adicionar, encontrar
 * e listar veículos em seu estoque. Ela interage com um componente de armazenamento, que implementa a interface `I_Armazenamento`.
 */
package com.example.concessionaria;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import org.bson.Document;
import org.bson.types.Decimal128;

public class Concessionaria {
    // Componente de armazenamento para lidar com o armazenamento de veículos
    public I_Armazenamento armazenamento;

    /**
     * Construtor da classe `Concessionaria`.
     * @param armazenamento O componente de armazenamento a ser usado pela concessionária.
     */
    public Concessionaria(I_Armazenamento armazenamento) {
        this.armazenamento = armazenamento;
    }

    /**
     * Adiciona um veículo ao estoque da concessionária.
     * @param veiculo O veículo a ser adicionado.
     * @return `true` se a adição for bem-sucedida, `false` em caso de erro.
     */
    public Boolean AdicionaVeiculoEstoque(Veiculo veiculo){
        return this.armazenamento.addVeiculoEstoque(veiculo);
    }

    /**
     * Encontra um veículo no estoque pelo número de placa e exibe suas informações.
     * @param placa O número de placa do veículo a ser encontrado.
     */
    public void EncontraVeiculoEstoque(String placa){
        Document doc = this.armazenamento.findVeiculoEstoque(placa);

        if (doc == null){
            System.out.println("Veículo com placa (" + placa+ ") não encontrado no estoque.");
        }else {
            this.PrintDocument(doc);
        }
    }

    /**
     * Imprime as informações de um documento MongoDB formatado.
     * @param documento O documento a ser impresso.
     */
    private void PrintDocument(Document documento){
        System.out.println("---------------------------------");
        formatAndPrintField("Tipo", documento.getString("tipo"));
        formatAndPrintField("Placa", documento.getString("placa"));
        formatAndPrintField("Marca", documento.getString("marca"));
        formatAndPrintField("Modelo", documento.getString("modelo"));
        formatAndPrintField("Ano de Fabricação", documento.getString("ano_de_fabricacao"));
        formatAndPrintField("Preço", documento.get("preco"));

        if ("Carro".equals(documento.getString("tipo"))) {
            formatAndPrintField("Número de Portas", documento.get("numero_de_portas"));
        } else if ("Motocicleta".equals(documento.getString("tipo"))) {
            formatAndPrintField("Cilindradas", documento.get("cilindradas"));
        }
        System.out.println("---------------------------------");
    }

    /**
     * Formata e imprime um campo (label) e seu valor.
     * @param label O rótulo do campo.
     * @param value O valor do campo.
     */
    private void formatAndPrintField(String label, Object value) {
        if (value != null) {
            String formattedValue = formatValue(value);
            System.out.println(label + ": " + formattedValue);
        }
    }

    /**
     * Formata o valor para exibição, incluindo a formatação de valores monetários.
     * @param value O valor a ser formatado.
     * @return O valor formatado como String.
     */
    private String formatValue(Object value) {
        if (value instanceof Decimal128) {
            BigDecimal decimalValue = ((Decimal128) value).bigDecimalValue();
            DecimalFormat df = new DecimalFormat("#,##0.00");
            return "R$" + df.format(decimalValue);
        } else {
            return String.valueOf(value);
        }
    }

    /**
     * Lista todos os veículos no estoque e imprime suas informações.
     */
    public void ListarTodosVeiculos() {
        List<Document> documentos = this.armazenamento.getAllVeiculos();
        
        for (Document documento : documentos) {
            this.PrintDocument(documento);
        }
    }
}
