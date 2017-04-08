package nl.gmta.btrfs.structure.stream;

import java.util.Objects;

public class BtrfsTruncateCommand extends BtrfsStreamCommand {
    private final String path;
    private final long size;

    public BtrfsTruncateCommand(BtrfsCommandHeader header, String path, long size) {
        super(header);

        this.path = Objects.requireNonNull(path);
        this.size = size;
    }

    public String getPath() {
        return this.path;
    }

    public long getSize() {
        return this.size;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s' size=%d}",
            this.getClass().getSimpleName(),
            this.header,
            this.path,
            this.size
        );
    }
}
