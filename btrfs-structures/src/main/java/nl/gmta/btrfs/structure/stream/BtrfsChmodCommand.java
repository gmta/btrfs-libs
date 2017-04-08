package nl.gmta.btrfs.structure.stream;

import java.util.Objects;

public class BtrfsChmodCommand extends BtrfsStreamCommand {
    private final String path;
    private final long mode;

    public BtrfsChmodCommand(BtrfsCommandHeader header, String path, long mode) {
        super(header);

        this.path = Objects.requireNonNull(path);
        this.mode = mode;
    }

    public String getPath() {
        return this.path;
    }

    public long getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s' mode=%o}",
            this.getClass().getSimpleName(),
            this.header,
            this.path,
            this.mode
        );
    }
}
