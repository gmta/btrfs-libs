package nl.gmta.btrfs.structure.stream;

public class BtrfsRenameCommand extends BtrfsStreamCommand {
    private final String path;
    private final String to;

    public BtrfsRenameCommand(BtrfsCommandHeader header, String path, String to) {
        super(header);

        this.path = path;
        this.to = to;
    }

    public String getPath() {
        return this.path;
    }

    public String getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s' to='%s'}",
            this.getClass().getSimpleName(),
            this.header,
            this.path,
            this.to
        );
    }
}
