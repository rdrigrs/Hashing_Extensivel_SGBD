package ds;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HashExt {
    private Directory directory = new Directory();

    // Para pegar o globalDepth
    int globalDepth = directory.getGlobalDepth();

    // Para pegar a directoryLines
    List<DirectoryLine> directoryLines = directory.getDirectoryLines();

    public String hashFunction(int year) {
        return Integer.toBinaryString(year);
    }

    public void insertRegistry(int year){
        String binaryYear = hashFunction(year);
        String key = binaryYear.substring(binaryYear.length()-1);

        //if (directory.isEmpty())
    }

    public void deleteRegistry(){

    }

    public void findRegistry(int year){
        String indexNumber = selectedIndexNumbers(year);

        // Achar a linha do diretório que contém o index desejado
        DirectoryLine directoryLine = null;
        for (DirectoryLine line : directoryLines) {
            if (line.getIndex().equals(indexNumber)) {
                directoryLine = line;
                break;
            }
        }

        if (directoryLine == null) {
            System.out.println("Registro não encontrado.");
            return;
        }

        // Encontre o bucket correspondente
        String bucket = directoryLine.getPointer();

        // abrir o arquivo .csv do bucket e buscar todos os registros que satisfazem o ano
        String csvFile = """
        ../bd/buckets/%s.csv""".formatted(bucket);

        List<String[]> csvBody = new ArrayList<>();
        try {
            // Ler o arquivo CSV
            CSVParser csvParser = new CSVParser(new FileReader(csvFile), CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : csvParser) {
                // Crie um array de strings para armazenar os valores desta linha
                String[] rowData = new String[csvRecord.size()];

                // Preencha o array com os valores desta linha
                for (int i = 0; i < csvRecord.size(); i++) {
                    rowData[i] = csvRecord.get(i);
                }
                // Adicione o array à lista de dados do CSV
                csvBody.add(rowData);
            }

            csvParser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String[]> registrys = new ArrayList<>();

        for (int i = 0; i < csvBody.size() ; i++) {
            if (csvBody.get(i)[2].equals(String.valueOf(year))) {
                registrys.add(csvBody.get(i));
            }
        }

        if (registrys.isEmpty()) {
            System.out.println("Registro não encontrado.");
        } else {
            System.out.println("Registro encontrado: " + registrys);
        }
    }

    public String selectedIndexNumbers(int year) {
        String binaryYear = hashFunction(year);
        int startNumber = binaryYear.length() - globalDepth;
        return binaryYear.substring(startNumber);
    }
}
