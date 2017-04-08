package nl.gmta.btrfs.structure.stream;

import java.util.Objects;
import java.util.UUID;

public class BtrfsSubvolCommand extends BtrfsStreamCommand {
    protected final String path;
    protected final UUID UUID;
    protected final long CTransID;

    public BtrfsSubvolCommand(BtrfsCommandHeader header, String path, UUID UUID, long CTransID) {
        super(header);

        this.path = Objects.requireNonNull(path);
        this.UUID = Objects.requireNonNull(UUID);
        this.CTransID = CTransID;
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

    @Override
    public String toString() {
        return String.format(
            "%s{header=%s path='%s' UUID=%s CTransID=%d}",
            this.getClass().getSimpleName(),
            this.header,
            this.path,
            this.UUID,
            this.CTransID
        );
    }
}
