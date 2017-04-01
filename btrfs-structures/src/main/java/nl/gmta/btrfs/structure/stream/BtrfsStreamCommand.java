package nl.gmta.btrfs.structure.stream;

public abstract class BtrfsStreamCommand extends BtrfsStreamElement {
    private final int length;
    private final BtrfsCommandType command;
    private final int crc;

    public BtrfsStreamCommand(int length, BtrfsCommandType command, int crc) {
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
}
