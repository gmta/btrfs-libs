package nl.gmta.btrfs.structure.stream;

import java.util.Objects;

public class BtrfsSetXAttrCommand extends BtrfsStreamCommand {
    private final String path;
    private final String name;
    private final byte[] data;

    public BtrfsSetXAttrCommand(BtrfsCommandHeader header, String path, String name, byte[] data) {
        super(header);

        this.path = Objects.requireNonNull(path);
        this.name = Objects.requireNonNull(name);
        this.data = Objects.requireNonNull(data);
    }

    public String getPath() {
        return this.path;
    }

    public String getName() {
        return this.name;
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s' name='%s' data=[%d bytes]}",
            this.getClass().getSimpleName(),
            this.header,
            this.path,
            this.name,
            this.data.length
        );
    }
}
