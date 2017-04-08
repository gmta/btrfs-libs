package nl.gmta.btrfs.structure.stream;

import java.util.Objects;
import java.util.UUID;

public class BtrfsSnapshotCommand extends BtrfsSubvolCommand {
    private final UUID cloneUUID;
    private final long cloneCTransID;

    public BtrfsSnapshotCommand(BtrfsCommandHeader header, String path, UUID UUID, long CTransID, UUID cloneUUID, long cloneCTransID) {
        super(header, path, UUID, CTransID);

        this.cloneUUID = Objects.requireNonNull(cloneUUID);
        this.cloneCTransID = cloneCTransID;
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
