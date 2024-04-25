import ds.HashExt;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("okaqui");
        HashExt hashExt;


        try {
            FileWriter writer = new FileWriter(outputFile);
            ArrayList<String> Lines = readInTXT(inputFile);

        int globalDepth = Integer.parseInt(Lines.remove(0).split("/")[1]);
        hashExt = new HashExt(globalDepth);

        ArrayList<String> outLines = null;

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

                if (correctData != null) {
                    outLines.add(hashExt.insertRegistry(year, Integer.parseInt(correctData.split(",")[0])));
                } else {
                    System.out.println("Não foi possível encontrar dados de compra correspondentes para o ano: " + year);
                }
            } else if (data[0].equals("REM")) {
                outLines.add(hashExt.deleteRegistry(year));
            } else if (data[0].equals("BUS")) {
                outLines.add(hashExt.findRegistry(year));

            }
        }

        writeOutTXT(outLines);

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

    public static ArrayList<String> readInTXT() {
        // Leitura do arquivo in.txt
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src\\res\\bd\\inout\\in.txt"));

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

    public static void writeOutTXT(ArrayList<String> Lines) {
        // Escrita do arquivo .txt
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src\\res\\bd\\inout\\out.txt"));

            for (String line : Lines) {
                writer.write(line + "\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}