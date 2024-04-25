import ds.Directory;

import ds.HashExt;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;

public class Main {
    // Aqui eu tô testando a leitura do arquivo CSV
    public static void main(String[] args) {
        HashExt hashExt = new HashExt();

        String inputFile = "in.txt";
        String outputFile = "out.txt;";


        try {
            FileWriter writer = new FileWriter(outputFile);
            ArrayList<String> Lines = readInTXT(inputFile);

            int globalDepth = Integer.parseInt(Lines.remove(0).split("/")[1]);

            for (String line : Lines) {
                String[] data = line.split(":");
                int year = Integer.parseInt(data[1]);
                if (data[0].equals("INC")) {
                    ArrayList<String> dataCompras = readCompras();
                    String correctData = null;

                    for (String compra : dataCompras) {
                        String[] compraData = compra.split(",");
                        if (compraData[2].equals(data[1])) {
                            correctData = compra;
                            break;
                        }
                    }

                    writer.write(hashExt.insertRegistry(Integer.parseInt(data[1]), Integer.parseInt(correctData.split(",")[0])));
                } else if (data[0].equals("REM")) {
                    writer.write(hashExt.deleteRegistry(year));
                } else if (data[0].equals("BUS")) {

                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readCompras() {
        String csvFile = "src\\res\\bd\\compras.csv";

        try {
            CSVParser csvParser = new CSVParser(new FileReader(csvFile), CSVFormat.DEFAULT);

            ArrayList<String> Lines = new ArrayList<>();
            String line;

            for (CSVRecord csvRecord : csvParser) {
                line = csvRecord.get(0) + ";" + csvRecord.get(1) + ";" + csvRecord.get(2);
                Lines.add(line);
            }
            csvParser.close();

            return Lines;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> readInTXT(String name) {
        // Leitura do arquivo in.txt
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src\\res\\bd\\inout\\" + name));

            ArrayList<String> Lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                Lines.add(line);
            }
            reader.close();
            return Lines;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeTXT(String name) {
        // Escrita do arquivo .txt
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src\\res\\bd\\" + name));

            writer.write("Testando essa linha aqui");
            writer.newLine();
            writer.write("Linha 2");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}