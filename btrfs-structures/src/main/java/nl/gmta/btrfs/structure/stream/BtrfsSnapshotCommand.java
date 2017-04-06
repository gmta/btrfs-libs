package nl.gmta.btrfs.structure.stream;

import java.util.Objects;
import java.util.UUID;

public class BtrfsSnapshotCommand extends BtrfsStreamCommand {
    private final String path;
    private final UUID UUID;
    private final long CTransID;
    private final UUID cloneUUID;
    private final long cloneCTransID;

    public BtrfsSnapshotCommand(BtrfsStreamCommandHeader header, String path, UUID UUID, long CTransID, UUID cloneUUID, long cloneCTransID) {
        super(header);

        this.path = Objects.requireNonNull(path);
        this.UUID = Objects.requireNonNull(UUID);
        this.CTransID = CTransID;
        this.cloneUUID = Objects.requireNonNull(cloneUUID);
        this.cloneCTransID = cloneCTransID;
    }

    public String getPath() {
        return this.path;
    }

    public UUID getUUID() {
        return this.UUID;
    }

    public long getCTransID() {
        return this.CTransID;
    }

    public UUID getCloneUUID() {
        return this.cloneUUID;
    }

    public long getCloneCTransID() {
        return this.cloneCTransID;
    }

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s' UUID=%s CTransID=%d cloneUUID=%s cloneCTransID=%d}",
            this.getClass().getSimpleName(),
            this.header,
            this.path,
            this.UUID,
            this.CTransID,
            this.cloneUUID,
            this.cloneCTransID
        );
    }
}
