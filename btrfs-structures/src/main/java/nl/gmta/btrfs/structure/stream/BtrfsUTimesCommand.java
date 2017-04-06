package nl.gmta.btrfs.structure.stream;

import java.util.Objects;

public class BtrfsUTimesCommand extends BtrfsStreamCommand {
    private final String path;
    private final BtrfsTimespec atime;
    private final BtrfsTimespec mtime;
    private final BtrfsTimespec ctime;

    public BtrfsUTimesCommand(BtrfsStreamCommandHeader header, String path, BtrfsTimespec atime, BtrfsTimespec mtime, BtrfsTimespec ctime) {
        super(header);

        this.path = Objects.requireNonNull(path);
        this.atime = Objects.requireNonNull(atime);
        this.mtime = Objects.requireNonNull(mtime);
        this.ctime = Objects.requireNonNull(ctime);
    }

    public String getPath() {
        return this.path;
    }

    public BtrfsTimespec getATime() {
        return this.atime;
    }

    public BtrfsTimespec getMTime() {
        return this.mtime;
    }

    public BtrfsTimespec getCTime() {
        return this.ctime;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s' atime=%s ctime=%s mtime=%s}",
            this.getClass().getSimpleName(),
            this.header,
            this.path,
            this.atime,
            this.ctime,
            this.mtime
        );
    }
}
