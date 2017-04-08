package nl.gmta.btrfs.structure.stream;

import java.util.Objects;

public class BtrfsRemoveXAttrCommand extends BtrfsStreamCommand {
    private final String path;
    private final String name;

    public BtrfsRemoveXAttrCommand(BtrfsCommandHeader header, String path, String name) {
        super(header);

        this.path = Objects.requireNonNull(path);
        this.name = Objects.requireNonNull(name);
    }

    public String getPath() {
        return this.path;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s' name='%s'}",
            this.getClass().getSimpleName(),
            this.header,
            this.path,
            this.name
        );
    }
}
