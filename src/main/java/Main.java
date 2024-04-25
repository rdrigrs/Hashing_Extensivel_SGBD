import ds.Directory;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;

public class Main {
    // Aqui eu tô testando a leitura do arquivo CSV
    public static void main(String[] args) {
//        Directory dir = new Directory("0", 2004, 1);
//        System.out.println("0000000");

        String csvFile = "src\\res\\bd\\compras.csv";

        try {
            CSVParser csvParser = new CSVParser(new FileReader(csvFile), CSVFormat.DEFAULT);

            for (CSVRecord csvRecord : csvParser) {
                // Acessar os valores por índice de coluna
                String columnOne = csvRecord.get(0);
                String columnTwo = csvRecord.get(1);
                String columnThree = csvRecord.get(2);
                // etc.

                System.out.println("Coluna 1: " + columnOne + ", Coluna 2: " + columnTwo + ", Coluna 3: " + columnThree);
            }

            csvParser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Escrita do arquivo .txt
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src\\res\\bd\\test.txt"));

            writer.write("Testando essa linha aqui");
            writer.newLine();
            writer.write("Linha 2");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Leitura do arquivo .txt
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src\\res\\bd\\test.txt"));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}