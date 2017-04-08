package nl.gmta.btrfs.structure.stream;

import java.util.Objects;
import java.util.UUID;

public class BtrfsCloneCommand extends BtrfsStreamCommand {
    private final long fileOffset;
    private final long cloneSize;
    private final String path;
    private final UUID cloneUUID;
    private final long cloneCTransID;
    private final String clonePath;
    private final long cloneOffset;

    public BtrfsCloneCommand(BtrfsCommandHeader header, long fileOffset, long cloneSize, String path, UUID cloneUUID,
            long cloneCTransID, String clonePath, long cloneOffset) {
        super(header);

        this.fileOffset = fileOffset;
        this.cloneSize = cloneSize;
        this.path = Objects.requireNonNull(path);
        this.cloneUUID = Objects.requireNonNull(cloneUUID);
        this.cloneCTransID = cloneCTransID;
        this.clonePath = Objects.requireNonNull(clonePath);
        this.cloneOffset = cloneOffset;
    }

    public long getFileOffset() {
        return this.fileOffset;
    }

    public long getCloneSize() {
        return this.cloneSize;
    }

    public String getPath() {
        return this.path;
    }

    public UUID getCloneUUID() {
        return this.cloneUUID;
    }

    public long getCloneCTransID() {
        return this.cloneCTransID;
    }

    public String getClonePath() {
        return this.clonePath;
    }

    public long getCloneOffset() {
        return this.cloneOffset;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s fileOffset=%d cloneSize=%d path='%s' cloneUUID=%s cloneCTransID=%d clonePath='%s' cloneOffset=%d}",
            this.getClass().getSimpleName(),
            this.header,
            this.fileOffset,
            this.cloneSize,
            this.path,
            this.cloneUUID,
            this.cloneCTransID,
            this.clonePath,
            this.cloneOffset
        );
    }
}
