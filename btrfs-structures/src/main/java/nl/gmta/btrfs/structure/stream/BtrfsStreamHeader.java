package nl.gmta.btrfs.structure.stream;

public class BtrfsStreamHeader extends BtrfsStreamElement {
    public static final String MAGIC = "btrfs-stream";

    private final int version;

    public BtrfsStreamHeader(int version) {
        this.version = version;
    }

    public int getVersion() {
        return this.version;
    }

    @Override
    public String toString() {
        return String.format("BtrfsStreamHeader{version=%d}", this.version);
    }
}
