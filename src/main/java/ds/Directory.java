package ds;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Directory {
    private int globalDepth;
    private List<DirectoryLine> directoryLines = new ArrayList<>();

    public Directory(int globalDepth) {
        this.globalDepth = globalDepth;
        String folder = "src\\res\\bd\\buckets\\";

        int numFiles = (int) Math.pow(2, globalDepth);

        for (int i = 0; i < numFiles; i++) {
            String binaryName = String.format("%" + globalDepth + "s", Integer.toBinaryString(i)).replace(' ', '0');
            if (binaryName.length() < globalDepth) {
                binaryName = String.format("%0" + globalDepth + "d", Integer.parseInt(binaryName, 2));
            }
            File file = new File(folder + binaryName + ".csv");
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }
    }

    public Directory(String key, int pKey, int year) {
        directoryLines.add(new DirectoryLine("0", 1, key));
        directoryLines.add(new DirectoryLine("1", 1, key));

        String folder = "src\\res\\bd\\buckets\\";

        try (CSVPrinter writer = new CSVPrinter(new FileWriter(folder + key + ".csv"), CSVFormat.DEFAULT)) {
            writer.printRecord(pKey, year);

            globalDepth = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            if (dl.getIndex() == index) {
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
        String filePath = "src\\res\\bd\\buckets\\" + key + ".csv";
        int numLines = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while (br.readLine() != null) {
                numLines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (numLines == 3);
    }

    public void writeOnBucket(String key, int pKey, int year) {
        String filePath = "src\\res\\bd\\buckets\\" + key + ".csv";
        ;

        try (CSVPrinter writer = new CSVPrinter(new FileWriter(filePath, true), CSVFormat.DEFAULT)) {
            writer.printRecord(pKey, year);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void relocateRegistries(String key, int newLocalDepth) {
        String newKey0 = "0" + key;
        String newKey1 = "1" + key;
        String binaryYear, tempKey;
        String inputFilePath = "src\\res\\bd\\buckets\\" + key + ".csv";
        String outputFilePath1 = "src\\res\\bd\\buckets\\" + newKey0 + ".csv";
        String outputFilePath2 = "src\\res\\bd\\buckets\\" + newKey1 + ".csv";
        List<List<String>> dataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);) {

            for (CSVRecord record : parser) {
                List<String> recordList = new ArrayList<>();
                for (String value : record) {
                    recordList.add(value);
                }
                dataList.add(recordList);
            }

            boolean deleted = new File(inputFilePath).delete();
            System.out.println("Input file deleted: " + deleted);

            try (CSVPrinter printer0 = new CSVPrinter(new FileWriter(outputFilePath1), CSVFormat.DEFAULT);
                 CSVPrinter printer1 = new CSVPrinter(new FileWriter(outputFilePath2), CSVFormat.DEFAULT);) {

                for (List<String> record : dataList) {
                    binaryYear = Integer.toBinaryString(Integer.parseInt(record.get(1)));
                    tempKey = binaryYear.substring(binaryYear.length()-newLocalDepth);

                    if (tempKey.equals("0" + key)){
                        printer0.printRecord(record);
                    } else {
                        printer1.printRecord(record);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (DirectoryLine dl : directoryLines){
            if (dl.getPointer().equals(key)) {
                dl.setLocalDepth(newLocalDepth);
                if (dl.getIndex().equals("0"+ key)){
                    dl.setPointer(newKey0);
                } else {
                    dl.setPointer(newKey1);
                }
            }
        }

        if (globalDepth < newLocalDepth){
            globalDepth = newLocalDepth;
        }
    }
}
