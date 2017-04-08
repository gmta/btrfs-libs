package nl.gmta.btrfs.structure.stream;

import java.util.Objects;

public class BtrfsChownCommand extends BtrfsStreamCommand {
    private final String path;
    private final long uid;
    private final long gid;

    public BtrfsChownCommand(BtrfsCommandHeader header, String path, long uid, long gid) {
        super(header);

        this.path = Objects.requireNonNull(path);
        this.uid = uid;
        this.gid = gid;
    }

    public String getPath() {
        return this.path;
    }

    public long getUid() {
        return this.uid;
    }

    public long getGid() {
        return this.gid;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s' uid=%d gid=%d}",
            this.getClass().getSimpleName(),
            this.header,
            this.path,
            this.uid,
            this.gid
        );
    }
}
