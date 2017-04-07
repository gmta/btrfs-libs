package nl.gmta.btrfs.structure.stream;

import java.util.Objects;

public abstract class BtrfsStreamCommand extends BtrfsStreamElement {
    protected final BtrfsCommandHeader header;

    BtrfsStreamCommand(BtrfsCommandHeader header) {
        this.header = Objects.requireNonNull(header);
    }

    public BtrfsCommandHeader getHeader() {
        return this.header;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s}",
            this.getClass().getSimpleName(),
            this.header
        );
    }
}
