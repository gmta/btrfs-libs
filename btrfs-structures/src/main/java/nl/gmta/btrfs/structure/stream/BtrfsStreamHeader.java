package nl.gmta.btrfs.structure.stream;

public class BtrfsStreamHeader extends BtrfsStreamElement {
    public static final String MAGIC = "btrfs-stream";

    private int version;

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return String.format("BtrfsStreamHeader{version=%d}", this.version);
    }
}
