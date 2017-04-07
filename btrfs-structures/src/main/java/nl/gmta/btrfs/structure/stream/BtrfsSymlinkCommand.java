package nl.gmta.btrfs.structure.stream;

public class BtrfsSymlinkCommand extends BtrfsInodeCommand {
    private final String link;

    public BtrfsSymlinkCommand(BtrfsCommandHeader header, String path, long inode, String link) {
        super(header, path, inode);

        this.link = link;
    }

    public String getLink() {
        return this.link;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s' inode=%d link='%s'}",
            this.getClass().getSimpleName(),
            this.header,
            this.path,
            this.inode,
            this.link
        );
    }
}
