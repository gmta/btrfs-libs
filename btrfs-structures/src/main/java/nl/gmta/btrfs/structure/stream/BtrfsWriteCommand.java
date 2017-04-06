package nl.gmta.btrfs.structure.stream;

import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

public class BtrfsWriteCommand extends BtrfsStreamCommand {
    private final String path;
    private final long fileOffset;
    private final int dataSize;
    private final ReadableByteChannel data;

    public BtrfsWriteCommand(BtrfsStreamCommandHeader header, String path, long fileOffset, int dataSize, ReadableByteChannel data) {
        super(header);

        this.path = Objects.requireNonNull(path);
        this.fileOffset = fileOffset;
        this.dataSize = dataSize;
        this.data = Objects.requireNonNull(data);
    }

    public String getPath() {
        return this.path;
    }

    public long getFileOffset() {
        return this.fileOffset;
    }

    public int getDataSize() {
        return this.dataSize;
    }

    public ReadableByteChannel getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s' fileOffset=%d dataSize=%d data=[on-demand]}",
            this.getClass().getSimpleName(),
            this.header,
            this.path,
            this.fileOffset,
            this.dataSize
        );
    }
}
