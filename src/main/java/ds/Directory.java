package ds;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Directory {
    private int globalDepth;
    private List<DirectoryLine> directoryLines = new ArrayList<>();

    public Directory(){
        globalDepth = 0;
    }
    public Directory(String key, int year, int pKey){
        directoryLines.add(new DirectoryLine("0", 1, key));
        directoryLines.add(new DirectoryLine("1", 1, key));
        globalDepth = 1;

        String folder = "../../../res/buckets/";
        String line = pKey + "," + year;
        try (CSVPrinter writer = new CSVPrinter(new FileWriter(folder + key), CSVFormat.DEFAULT.withHeader("Year", "PK"))) {
            System.out.println("okaqui");

            writer.printRecord("Year", "PK"); // header
            writer.println(); // newline

            writer.printRecord(line); // write the line
            writer.println(); // newline

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("errror");
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

    public boolean isEmpty(){
        return(globalDepth == 0);
    }

    public void duplicateDirectory() {
        List<DirectoryLine> auxList = new ArrayList<>();
        for (DirectoryLine item : directoryLines) {
            auxList.add(item);
            auxList.add(item);
        }
        for (int i = 0; i < auxList.size(); i++) {
            if (i%2 == 0) {
                auxList.get(i).setIndex("0" + auxList.get(i).getIndex());
            } else {
                auxList.get(i).setIndex("1" + auxList.get(i).getIndex());
            }
        }
        directoryLines = auxList;
    }
}
