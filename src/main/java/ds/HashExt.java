package ds;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HashExt {
    private Directory directory;
    private int globalDepth;

    // Para pegar a directoryLines
    List<DirectoryLine> directoryLines;

    public HashExt(int globalDepth){
        this.globalDepth = globalDepth;
        directory = new Directory(globalDepth);
        directoryLines = directory.getDirectoryLines();
    }

    public String hashFunction(int year) {
        return Integer.toBinaryString(year);
    }

    public String insertRegistry(int pKey, int year){
        String binaryYear = hashFunction(year);
        String key, index;
        index = binaryYear.substring(binaryYear.length() - globalDepth);
        int localDepth = directory.findLocalDepth(index);

        if (directory.isEmpty()){
            key = binaryYear.substring(binaryYear.length()-1);
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

        String txtFile = bucket + ".txt";

        List<String[]> txtBody = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(txtFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                txtBody.add(rowData);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String[]> registriesToDelete = new ArrayList<>();
        for (String[] registry : txtBody) {
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
        txtBody.removeAll(registriesToDelete);


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile))) {
            for (String[] registry : txtBody) {
                System.out.println(registry);
                writer.write(String.join(",", registry) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Se o bucket estiver vazio, delete a referência para ele no diretório
        if (txtBody.isEmpty()) {
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

    public String findRegistry(int year) {
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
        String txtFile = """
        %s.txt""".formatted(bucket);

        List<String[]> bucketContent = new ArrayList<>();
        try {
            // Ler o arquivo TXT
            BufferedReader reader = new BufferedReader(new FileReader(txtFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                bucketContent.add(rowData);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String[]> registrys = new ArrayList<>();

        for (int i = 0; i < bucketContent.size() ; i++) {
            if (bucketContent.get(i)[1].equals(String.valueOf(year))) {
                registrys.add(bucketContent.get(i));
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
