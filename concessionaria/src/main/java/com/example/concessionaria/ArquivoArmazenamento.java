/**
 * A classe `ArquivoArmazenamento` implementa a interface `I_Armazenamento` para gerenciar o estoque de veículos
 * armazenados em um arquivo Excel (.xlsx). Ela fornece métodos para adicionar veículos ao estoque, encontrar
 * veículos por placa e obter uma lista de todos os veículos no estoque.
 */
package com.example.concessionaria;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ArquivoArmazenamento implements I_Armazenamento {
    private Set<String> placasExistentes = new HashSet<>();
    private List<Veiculo> estoque = new ArrayList<>();
    private static final String FILE_NAME = "estoque.xlsx"; // Nome do arquivo de Excel

    /**
     * Adiciona um veículo ao estoque e salva os dados no arquivo Excel.
     * @param veiculo O veículo a ser adicionado ao estoque.
     * @return `true` se o veículo foi adicionado com sucesso, `false` caso contrário.
     */
    @Override
    public boolean addVeiculoEstoque(Veiculo veiculo) {
        System.out.println("---");
        System.out.println("  Adicionando veículo ao arquivo: " + veiculo.modelo);
        
        // Verifique se já existe um veículo com a mesma placa no estoque
        if (placasExistentes.contains(veiculo.placa)) {
            System.out.println("\n  [ERRO] - Já existe um veículo com a mesma placa no estoque.");
            System.out.println("---");
            return false; // Veículo com a mesma placa já existe, não é adicionado
        }

        this.estoque.add(veiculo);
        placasExistentes.add(veiculo.placa); // Adicione a placa ao conjunto de placas existentes
        salvarDadosNoArquivo();
        System.out.println("---");
        return true;
    }

    /**
     * Encontra um veículo no estoque com base na placa fornecida.
     * @param placa A placa do veículo a ser encontrado.
     * @return Um documento representando o veículo encontrado ou `null` se o veículo não foi encontrado.
     */
    @Override
    public Document findVeiculoEstoque(String placa) {

        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(fis)) {
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);

                Iterator<Row> rowIterator = sheet.iterator();
                // Pule a primeira linha (cabeçalho)
                if (rowIterator.hasNext()) {
                    rowIterator.next();
                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Cell placaCell = row.getCell(0); // Suponha que o número de placa esteja na primeira coluna (0)

                    if (placaCell != null && placaCell.getCellType() == CellType.STRING) {
                        String placaVeiculo = placaCell.getStringCellValue();
                        
                        // Verifica se encontrou o veiculo com a placa informada
                        if (placaVeiculo.equals(placa)) {
                            // Coletar nomes de cabeçalho formatados
                            List<String> headers = getHeaders(sheet);
                            Document documento = getRowDocument(sheet, row, headers);
                            return documento; // Retorna o documento do veículo encontrado
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler dados do arquivo: " + e.getMessage());
        }
    
        // Veículo não encontrado
        return null;
    }

    /**
     * Obtém uma lista de todos os veículos no estoque em forma de documentos.
     * @return Uma lista de documentos representando todos os veículos no estoque.
     */
    @Override
    public List<Document> getAllVeiculos() {
        List<Document> documentos = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(fis)) {
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);

                // Pule a primeira linha (cabeçalho)
                Iterator<Row> rowIterator = sheet.iterator();
                if (rowIterator.hasNext()) {
                    rowIterator.next();
                }

                // Coletar nomes de cabeçalho formatados
                List<String> headers = getHeaders(sheet);

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Document documento = getRowDocument(sheet, row, headers);
                    documentos.add(documento);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler dados do arquivo: " + e.getMessage());
            return Collections.emptyList();
        }

        return documentos;
    }

    /**
     * Obtém o valor de uma célula de uma planilha como uma String.
     * @param cell A célula da planilha.
     * @return O valor da célula como uma String.
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return null;
        }
    }

    /**
     * Formata o cabeçalho removendo diacríticos, convertendo para minúsculas e substituindo espaços por sublinhados.
     * @param header O cabeçalho a ser formatado.
     * @return O cabeçalho formatado.
     */
    private String formatHeader(String header) {
        String normalizedHeader = Normalizer.normalize(header, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String withoutDiacritics = pattern.matcher(normalizedHeader).replaceAll("");
        return withoutDiacritics.toLowerCase().replace(" ", "_");
    }

    /**
     * Obtém o cabeçalho formatado de uma planilha.
     * @param sheet A planilha da qual o cabeçalho será obtido.
     * @return Uma lista de nomes de cabeçalho formatados.
     */
    private List<String> getHeaders(Sheet sheet){
        List<String> headers = new ArrayList<>();

        // Coletar nomes de cabeçalho formatados
        Row headerRow = sheet.getRow(0);
        for (int colNum = 0; colNum < headerRow.getLastCellNum(); colNum++) {
            Cell headerCell = headerRow.getCell(colNum);
            String formattedHeader = formatHeader(headerCell.getStringCellValue());
            headers.add(formattedHeader);
        }

        return headers;
    }

    /**
     * Converte uma linha de dados de uma planilha em um documento MongoDB.
     * @param sheet A planilha da qual a linha de dados foi obtida.
     * @param row A linha de dados a ser convertida.
     * @param headers Os nomes de cabeçalho formatados.
     * @return Um documento MongoDB representando a linha de dados.
     */
    private Document getRowDocument(Sheet sheet, Row row, List<String> headers){
        Document documento = new Document();

        for (int colNum = 0; colNum < row.getLastCellNum(); colNum++) {
            Cell cell = row.getCell(colNum);
            String header = headers.get(colNum);
            String value = getCellValueAsString(cell);
            
            documento.append(header, value);
        }

        documento.append("tipo", sheet.getSheetName());
        return documento;
    } 

    /**
     * Salva os dados do estoque no arquivo Excel.
     */
    private void salvarDadosNoArquivo() {
        try (Workbook workbook = new XSSFWorkbook()) {
            for (Veiculo veiculoEstoque : this.estoque) {
                String tipo = veiculoEstoque.getTipo();
                Sheet sheet = workbook.getSheet(tipo);
    
                if (sheet == null) {
                    sheet = workbook.createSheet(tipo);
                    // Crie um cabeçalho específico para o tipo de veículo
                    veiculoEstoque.arquivoCabecalho(sheet);
                }
    
                // Adicione os dados do veículo à planilha
                veiculoEstoque.toRow(sheet);
            }
    
            // Salve o arquivo
            try (FileOutputStream outputStream = new FileOutputStream(FILE_NAME)) {
                workbook.write(outputStream);
                System.out.println("\n  [SUCESSO] - Dados salvos no arquivo: " + FILE_NAME);
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados no arquivo: " + e.getMessage());
        }
    }
}
