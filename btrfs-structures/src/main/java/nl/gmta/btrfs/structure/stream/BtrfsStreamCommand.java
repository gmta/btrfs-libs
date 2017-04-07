package nl.gmta.btrfs.structure.stream;

import java.util.Objects;

public abstract class BtrfsStreamCommand extends BtrfsStreamElement {
    protected final BtrfsStreamCommandHeader header;

    BtrfsStreamCommand(BtrfsStreamCommandHeader header) {
        this.header = Objects.requireNonNull(header);
    }

    public BtrfsStreamCommandHeader getHeader() {
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
