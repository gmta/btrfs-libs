package nl.gmta.btrfs.structure.stream;

public enum BtrfsAttributeType {
    UUID           (0x01),
    CTRANSID       (0x02),
    INO            (0x03),
    SIZE           (0x04),
    MODE           (0x05),
    UID            (0x06),
    GID            (0x07),
    RDEV           (0x08),
    CTIME          (0x09),
    MTIME          (0x0A),
    ATIME          (0x0B),
    OTIME          (0x0C),
    XATTR_NAME     (0x0D),
    XATTR_DATA     (0x0E),
    PATH           (0x0F),
    PATH_TO        (0x10),
    PATH_LINK      (0x11),
    FILE_OFFSET    (0x12),
    DATA           (0x13),
    CLONE_UUID     (0x14),
    CLONE_CTRANSID (0x15),
    CLONE_PATH     (0x16),
    CLONE_OFFSET   (0x17),
    CLONE_LEN      (0x18);

    private int value;

    BtrfsAttributeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
