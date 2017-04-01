package nl.gmta.btrfs.structure.stream;

public abstract class BtrfsStreamCommand extends BtrfsStreamElement {
    private final int length;

    public BtrfsStreamCommand(int length) {
        this.length = length;
    }

    public int getLength() {
        return this.length;
    }
}
