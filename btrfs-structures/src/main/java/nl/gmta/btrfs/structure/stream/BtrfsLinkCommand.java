package nl.gmta.btrfs.structure.stream;

public class BtrfsLinkCommand extends BtrfsStreamCommand {
    private final String path;
    private final String link;

    public BtrfsLinkCommand(BtrfsCommandHeader header, String path, String link) {
        super(header);

        this.path = path;
        this.link = link;
    }

    public String getPath() {
        return this.path;
    }

    public String getLink() {
        return this.link;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s' link='%s'}",
            this.getClass().getSimpleName(),
            this.header,
            this.path,
            this.link
        );
    }
}
