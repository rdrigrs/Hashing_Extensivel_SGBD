package ds;

public class DirectoryLine {
    private String index;
    private int localDepth;
    private String pointer;

    public DirectoryLine(String index, int localDepth, String pointer) {
        this.index = index;
        this.localDepth = localDepth;
        this.pointer = pointer;
    }

    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }

    public int getLocalDepth() {
        return localDepth;
    }
    public void setLocalDepth(int localDepth) {
        this.localDepth = localDepth;
    }

    public String getPointer() {
        return pointer;
    }
    public void setPointer(String pointer) {
        this.pointer = pointer;
    }
}
