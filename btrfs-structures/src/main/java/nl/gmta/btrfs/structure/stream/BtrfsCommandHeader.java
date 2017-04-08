package nl.gmta.btrfs.structure.stream;

import java.util.Objects;

public class BtrfsCommandHeader {
    private final int length;
    private final BtrfsCommandType command;
    private final int crc;

    public BtrfsCommandHeader(int length, BtrfsCommandType command, int crc) {
        this.length = length;
        this.command = Objects.requireNonNull(command);
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
        return String.format(
            "%s{length=%d command=%s crc=%08x}",
            this.getClass().getSimpleName(),
            this.length,
            this.command,
            this.crc
        );
    }
}
