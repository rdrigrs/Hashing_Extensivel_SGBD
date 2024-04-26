import ds.HashExt;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        HashExt hashExt;

        ArrayList<String> Lines = readInTXT();

        int globalDepth = Integer.parseInt(Lines.remove(0).split("/")[1]);
        hashExt = new HashExt(globalDepth);

        ArrayList<String> outLines = new ArrayList<>();

        outLines.add("PG/" + globalDepth);

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
                    outLines.add(hashExt.insertRegistry(Integer.parseInt(correctData.split(",")[0]), year));
                }else {}
            } else if (data[0].equals("REM")) {
                outLines.add(hashExt.deleteRegistry(year));
            } else if (data[0].equals("BUS")) {
                outLines.add(hashExt.findRegistry(year));

            }
        }

        outLines.add("P/" + globalDepth);

        writeOutTXT(outLines);

    }

    public static ArrayList<String> readCompras() {
        String csvFile = "../java/bd/compras.csv";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            ArrayList<String> lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> readInTXT() {
        // Leitura do arquivo in.txt
        try {
            BufferedReader reader = new BufferedReader(new FileReader("../java/bd/inout/in.txt"));

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
            BufferedWriter writer = new BufferedWriter(new FileWriter("../java/bd/inout/out.txt"));

            for (String line : Lines) {
                writer.write(line + "\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}