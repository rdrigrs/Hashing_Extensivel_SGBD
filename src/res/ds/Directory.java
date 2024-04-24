package res.ds;

import java.util.List;

public class Directory {
    private int globalDepth;
    private List<DirectoryLine> directoryLines;

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
}
