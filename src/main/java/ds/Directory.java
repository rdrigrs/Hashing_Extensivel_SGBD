package ds;

//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVParser;
//import org.apache.commons.csv.CSVPrinter;
//import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Directory {
    private int globalDepth;
    private List<DirectoryLine> directoryLines = new ArrayList<>();

    public Directory(int globalDepth) {
        this.globalDepth = globalDepth;
        String folder = "";

        int numFiles = (int) Math.pow(2, globalDepth);

        for (int i = 0; i < numFiles; i++) {
            String binaryName = String.format("%" + globalDepth + "s", Integer.toBinaryString(i)).replace(' ', '0');
            if (binaryName.length() < globalDepth) {
                binaryName = String.format("%0" + globalDepth + "d", Integer.parseInt(binaryName, 2));
            }
            directoryLines.add(new DirectoryLine(binaryName, globalDepth, binaryName));
            File file = new File(folder + binaryName + ".txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }
    }


//    public Directory(String key, int pKey, int year) {
//        directoryLines.add(new DirectoryLine("0", 1, key));
//        directoryLines.add(new DirectoryLine("1", 1, key));
//
//        String folder = "src\\res\\bd\\buckets\\";
//
//        try (PrintWriter writer = new PrintWriter(new FileWriter(folder + key + ".txt"))) {
//            writer.println(pKey + "," + year);
//
//            globalDepth = 1;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    public int getGlobalDepth() {
        return globalDepth;
    }

    public void setGlobalDepth(int globalDepth) {
        this.globalDepth = globalDepth;
    }

    public List<DirectoryLine> getDirectoryLines() {
        return directoryLines;
    }

    public void setDirectoryLines(List<DirectoryLine> directoryLines) {
        this.directoryLines = directoryLines;
    }

    public int findLocalDepth(String index) {
        for (DirectoryLine dl : directoryLines) {
//            System.out.print(dl.getIndex() + " ");
            if (dl.getIndex().equals(index)) {
                return dl.getLocalDepth();
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return (globalDepth == 0);
    }

    public void duplicateDirectory() {
        List<DirectoryLine> auxList = new ArrayList<>();
        for (DirectoryLine item : directoryLines) {
            auxList.add(item);
            auxList.add(item);
        }
        for (int i = 0; i < auxList.size(); i++) {
            if (i % 2 == 0) {
                auxList.get(i).setIndex("0" + auxList.get(i).getIndex());
            } else {
                auxList.get(i).setIndex("1" + auxList.get(i).getIndex());
            }
        }
        directoryLines = auxList;
    }

    public boolean isBucketFull(String key) {
        String filePath = key + ".txt";
        int numLines = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                numLines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (numLines == 3);
    }

    public void writeOnBucket(String key, int pKey, int year) {
        String filePath = key + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println(pKey + "," + year);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void relocateRegistries(String key, int newLocalDepth) {
        String newKey0 = "0" + key;
        String newKey1 = "1" + key;
        String binaryYear, tempKey;
        String inputFilePath = key + ".txt";
        String outputFilePath1 = newKey0 + ".txt";
        String outputFilePath2 = newKey1 + ".txt";
        List<String> dataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));) {

            String line;
            while ((line = reader.readLine()) != null) {
                dataList.add(line);
            }

            boolean deleted = new File(inputFilePath).delete();
            System.out.println("Input file deleted: " + deleted);

            try (PrintWriter printer0 = new PrintWriter(new FileWriter(outputFilePath1));
                 PrintWriter printer1 = new PrintWriter(new FileWriter(outputFilePath2));) {

                for (String record : dataList) {
                    String[] fields = record.split(","); // assume spaces as separators
                    binaryYear = Integer.toBinaryString(Integer.parseInt(fields[1]));
                    tempKey = binaryYear.substring(binaryYear.length() - newLocalDepth);

                    if (tempKey.equals("0" + key)) {
                        printer0.println(record);
                    } else {
                        printer1.println(record);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (DirectoryLine dl : directoryLines) {
            if (dl.getPointer().equals(key)) {
                dl.setLocalDepth(newLocalDepth);
                if (dl.getIndex().equals("0" + key)) {
                    dl.setPointer(newKey0);
                } else {
                    dl.setPointer(newKey1);
                }
            }
        }

        if (globalDepth < newLocalDepth) {
            globalDepth = newLocalDepth;
        }
    }
}
