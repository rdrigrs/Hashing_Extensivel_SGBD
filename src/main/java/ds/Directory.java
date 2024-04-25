package ds;

import java.util.List;

public class Directory {
    private int globalDepth;
    private List<DirectoryLine> directoryLines;

    public Directory(){
        setDirectoryLines(new ArrayList<>());
        setGlobalDepth(0);
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
