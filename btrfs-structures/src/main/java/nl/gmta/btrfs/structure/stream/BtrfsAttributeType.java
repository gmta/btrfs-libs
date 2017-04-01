package nl.gmta.btrfs.structure.stream;

public enum BtrfsAttributeType {
    BTRFS_SEND_A_UUID           (0x01),
    BTRFS_SEND_A_CTRANSID       (0x02),
    BTRFS_SEND_A_INO            (0x03),
    BTRFS_SEND_A_SIZE           (0x04),
    BTRFS_SEND_A_MODE           (0x05),
    BTRFS_SEND_A_UID            (0x06),
    BTRFS_SEND_A_GID            (0x07),
    BTRFS_SEND_A_RDEV           (0x08),
    BTRFS_SEND_A_CTIME          (0x09),
    BTRFS_SEND_A_MTIME          (0x0A),
    BTRFS_SEND_A_ATIME          (0x0B),
    BTRFS_SEND_A_OTIME          (0x0C),
    BTRFS_SEND_A_XATTR_NAME     (0x0D),
    BTRFS_SEND_A_XATTR_DATA     (0x0E),
    BTRFS_SEND_A_PATH           (0x0F),
    BTRFS_SEND_A_PATH_TO        (0x10),
    BTRFS_SEND_A_PATH_LINK      (0x11),
    BTRFS_SEND_A_FILE_OFFSET    (0x12),
    BTRFS_SEND_A_DATA           (0x13),
    BTRFS_SEND_A_CLONE_UUID     (0x14),
    BTRFS_SEND_A_CLONE_CTRANSID (0x15),
    BTRFS_SEND_A_CLONE_PATH     (0x16),
    BTRFS_SEND_A_CLONE_OFFSET   (0x17),
    BTRFS_SEND_A_CLONE_LEN      (0x18);

    private int value;

    BtrfsAttributeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
