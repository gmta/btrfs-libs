package nl.gmta.btrfs.structure.stream;

import nl.gmta.btrfs.structure.shared.IdentifiableEnum;
import nl.gmta.btrfs.structure.shared.IdentifiableEnumMap;

public enum BtrfsCommandType implements IdentifiableEnum<Integer> {
    SUBVOL        (0x01),
    SNAPSHOT      (0x02),
    MKFILE        (0x03),
    MKDIR         (0x04),
    MKNOD         (0x05),
    MKFIFO        (0x06),
    MKSOCK        (0x07),
    SYMLINK       (0x08),
    RENAME        (0x09),
    LINK          (0x0A),
    UNLINK        (0x0B),
    RMDIR         (0x0C),
    SET_XATTR     (0x0D),
    REMOVE_XATTR  (0x0E),
    WRITE         (0x0F),
    CLONE         (0x10),
    TRUNCATE      (0x11),
    CHMOD         (0x12),
    CHOWN         (0x13),
    UTIMES        (0x14),
    END           (0x15),
    UPDATE_EXTENT (0x16);

    private static final IdentifiableEnumMap<Integer, BtrfsCommandType> mapping = new IdentifiableEnumMap<>(BtrfsCommandType.values());

    private final int id;

    BtrfsCommandType(int id) {
        this.id = id;
    }

    public static BtrfsCommandType getById(Integer id) {
        return mapping.getById(id);
    }

    @Override
    public Integer getId() {
        return this.id;
    }
}
