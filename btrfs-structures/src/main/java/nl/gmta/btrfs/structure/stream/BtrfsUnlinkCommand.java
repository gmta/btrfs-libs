package nl.gmta.btrfs.structure.stream;

import java.util.Objects;

public class BtrfsUnlinkCommand extends BtrfsStreamCommand {
    private final String path;

    public BtrfsUnlinkCommand(BtrfsCommandHeader header, String path) {
        super(header);

        this.path = Objects.requireNonNull(path);
    }

    public String getPath() {
        return this.path;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s'}",
            this.getClass().getSimpleName(),
            this.header,
            this.path
        );
    }
}
