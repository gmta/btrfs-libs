package nl.gmta.btrfs.structure.stream;

import java.util.Objects;

public abstract class BtrfsInodeCommand extends BtrfsStreamCommand {
    protected final String path;
    protected final long inode;

    public BtrfsInodeCommand(BtrfsCommandHeader header, String path, long inode) {
        super(header);

        this.path = Objects.requireNonNull(path);
        this.inode = inode;
    }

    public String getPath() {
        return this.path;
    }

    public long getInode() {
        return this.inode;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s' inode=%d}",
            this.getClass().getSimpleName(),
            this.header,
            this.path,
            this.inode
        );
    }
}
