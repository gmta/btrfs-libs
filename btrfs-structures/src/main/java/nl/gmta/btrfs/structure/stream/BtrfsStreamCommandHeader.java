package nl.gmta.btrfs.structure.stream;

public class BtrfsStreamCommandHeader {
    private final int length;
    private final BtrfsCommandType command;
    private final int crc;

    public BtrfsStreamCommandHeader(int length, BtrfsCommandType command, int crc) {
        this.length = length;
        this.command = command;
        this.crc = crc;
    }

    public int getLength() {
        return this.length;
    }

    public BtrfsCommandType getCommand() {
        return this.command;
    }

    public int getCrc() {
        return this.crc;
    }

    @Override
    public String toString() {
        return String.format("%s{length=%d command=%s crc=%04X}", this.getClass().getSimpleName(), this.length, this.command, this.crc);
    }
}
