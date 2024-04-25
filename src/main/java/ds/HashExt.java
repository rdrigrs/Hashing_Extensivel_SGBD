package ds;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HashExt {
    private Directory directory;
    private int globalDepth;

    // Para pegar a directoryLines
    List<DirectoryLine> directoryLines = directory.getDirectoryLines();

    public HashExt(int globalDepth){
        this.globalDepth = globalDepth;
        directory = new Directory(globalDepth);
    }

    public String hashFunction(int year) {
        return Integer.toBinaryString(year);
    }

    public String insertRegistry(int year, int pKey){
        String binaryYear = hashFunction(year);
        String key, index;
        index = binaryYear.substring(binaryYear.length() - globalDepth);
        int localDepth = directory.findLocalDepth(index);


        if (directory.isEmpty()){
            key = binaryYear.substring(binaryYear.length()-1);
            directory = new Directory(key, pKey, year);
            globalDepth = directory.getGlobalDepth();
        } else {
            key = binaryYear.substring(binaryYear.length()-localDepth);
            if (!directory.isBucketFull(key)){
                directory.writeOnBucket(key, pKey, year);
            } else {
                localDepth++;
                directory.duplicateDirectory();
                directory.relocateRegistries(key, localDepth);
            }

        }

        return ("INC:" + year + "/" + globalDepth + "," + localDepth);
    }

    public String deleteRegistry(int year){
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
            return null;
        }

        // Encontrar o bucket correspondente
        String bucket = directoryLine.getPointer();

        // abrir o arquivo .csv do bucket e buscar todos os registros que satisfazem o ano
        String csvFile = "src\\res\\bd\\buckets\\" + bucket + ".csv";

        List<String[]> csvBody = new ArrayList<>();
        try {
            CSVParser csvParser = new CSVParser(new FileReader(csvFile), CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : csvParser) {
                String[] rowData = new String[csvRecord.size()];

                for (int i = 0; i < csvRecord.size(); i++) {
                    rowData[i] = csvRecord.get(i);
                }

                csvBody.add(rowData);
            }

            csvParser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String[]> registriesToDelete = new ArrayList<>();
        for (String[] registry : csvBody) {
            if (Integer.parseInt(registry[1]) == year) {
                registriesToDelete.add(registry);
            }
        }

        if (registriesToDelete.isEmpty()) {
            System.out.println("Registro não encontrado.");
            return null;
        }

        // Salvando o número de registros deletados
        int numberOfDeletedRegistries = registriesToDelete.size();

        // Delete o registro do csvBody
        csvBody.removeAll(registriesToDelete);

        // Reescreva o arquivo .csv sem o registro deletado
        try (CSVPrinter writer = new CSVPrinter(new FileWriter(csvFile), CSVFormat.DEFAULT)) {
            for (String[] registry : csvBody) {
                writer.printRecord((Object[]) registry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Se o bucket estiver vazio, delete a referência para ele no diretório
        if (csvBody.isEmpty()) {
            directoryLines.remove(directoryLine);
            String bucketPointer = directoryLine.getPointer();
            for (DirectoryLine line : directoryLines) {
                if (line.getPointer().equals(bucketPointer)) {
                    line.setLocalDepth(line.getLocalDepth() - 1);
                }
            }
        }

        int localDepth = directoryLine.getLocalDepth();

        return ("REM:" + year + "/" + numberOfDeletedRegistries + "," + globalDepth + "," + localDepth);
    }

    public String findRegistry(int year){
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
            return null;
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

        int numberOfSearchedRegistries = registrys.size();

        return ("BUS:" + year + "/" + numberOfSearchedRegistries);
    }

    public String selectedIndexNumbers(int year) {
        String binaryYear = hashFunction(year);
        int startNumber = binaryYear.length() - globalDepth;
        return binaryYear.substring(startNumber);
    }
}
