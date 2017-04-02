package nl.gmta.btrfs.structure.stream;

import nl.gmta.btrfs.structure.shared.IdentifiableEnum;
import nl.gmta.btrfs.structure.shared.IdentifiableEnumMap;

enum BtrfsAttributeType implements IdentifiableEnum<Integer> {
    UUID           (0x01, BtrfsValueType.UUID),
    CTRANSID       (0x02, BtrfsValueType.U64),
    INO            (0x03, BtrfsValueType.U64),
    SIZE           (0x04, BtrfsValueType.U64),
    MODE           (0x05, BtrfsValueType.U64),
    UID            (0x06, BtrfsValueType.U64),
    GID            (0x07, BtrfsValueType.U64),
    RDEV           (0x08, BtrfsValueType.U64),
    CTIME          (0x09, BtrfsValueType.TIMESPEC),
    MTIME          (0x0A, BtrfsValueType.TIMESPEC),
    ATIME          (0x0B, BtrfsValueType.TIMESPEC),
    OTIME          (0x0C, BtrfsValueType.TIMESPEC),
    XATTR_NAME     (0x0D, BtrfsValueType.STRING),
    XATTR_DATA     (0x0E, BtrfsValueType.BINARY),
    PATH           (0x0F, BtrfsValueType.STRING),
    PATH_TO        (0x10, BtrfsValueType.STRING),
    PATH_LINK      (0x11, BtrfsValueType.STRING),
    FILE_OFFSET    (0x12, BtrfsValueType.U64),
    DATA           (0x13, BtrfsValueType.BINARY),
    CLONE_UUID     (0x14, BtrfsValueType.UUID),
    CLONE_CTRANSID (0x15, BtrfsValueType.U64),
    CLONE_PATH     (0x16, BtrfsValueType.STRING),
    CLONE_OFFSET   (0x17, BtrfsValueType.U64),
    CLONE_LEN      (0x18, BtrfsValueType.U64);

    private static IdentifiableEnumMap<Integer, BtrfsAttributeType> mapping = new IdentifiableEnumMap<>(BtrfsAttributeType.values());

    private final int id;
    private final BtrfsValueType type;

    BtrfsAttributeType(int id, BtrfsValueType type) {
        this.id = id;
        this.type = type;
    }

    public static BtrfsAttributeType getById(Integer id) {
        return mapping.getById(id);
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    public BtrfsValueType getType() {
        return this.type;
    }
}
