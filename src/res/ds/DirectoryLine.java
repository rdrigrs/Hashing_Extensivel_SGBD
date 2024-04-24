package res.ds;

public class DirectoryLine {
    private String index;
    private int localDepth;
    private Bucket pointer;

    public DirectoryLine(String index, int localDepth, Bucket pointer) {
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

    public Bucket getPointer() {
        return pointer;
    }
    public void Bucket(Bucket pointer) {
        this.pointer = pointer;
    }
}
