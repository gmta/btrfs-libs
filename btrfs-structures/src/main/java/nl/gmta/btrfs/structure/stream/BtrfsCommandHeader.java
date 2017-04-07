package nl.gmta.btrfs.structure.stream;

public class BtrfsCommandHeader {
    private final int length;
    private final BtrfsCommandType command;
    private final long crc;

    public BtrfsCommandHeader(int length, BtrfsCommandType command, long crc) {
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

    public long getCrc() {
        return this.crc;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{length=%d command=%s crc=%08x}",
            this.getClass().getSimpleName(),
            this.length,
            this.command,
            this.crc
        );
    }
}
