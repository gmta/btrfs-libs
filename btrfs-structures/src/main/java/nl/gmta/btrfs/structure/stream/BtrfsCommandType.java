package nl.gmta.btrfs.structure.stream;

public enum BtrfsCommandType {
    BTRFS_SEND_C_SUBVOL        (0x01),
    BTRFS_SEND_C_SNAPSHOT      (0x02),
    BTRFS_SEND_C_MKFILE        (0x03),
    BTRFS_SEND_C_MKDIR         (0x04),
    BTRFS_SEND_C_MKNOD         (0x05),
    BTRFS_SEND_C_MKFIFO        (0x06),
    BTRFS_SEND_C_MKSOCK        (0x07),
    BTRFS_SEND_C_SYMLINK       (0x08),
    BTRFS_SEND_C_RENAME        (0x09),
    BTRFS_SEND_C_LINK          (0x0A),
    BTRFS_SEND_C_UNLINK        (0x0B),
    BTRFS_SEND_C_RMDIR         (0x0C),
    BTRFS_SEND_C_SET_XATTR     (0x0D),
    BTRFS_SEND_C_REMOVE_XATTR  (0x0E),
    BTRFS_SEND_C_WRITE         (0x0F),
    BTRFS_SEND_C_CLONE         (0x10),
    BTRFS_SEND_C_TRUNCATE      (0x11),
    BTRFS_SEND_C_CHMOD         (0x12),
    BTRFS_SEND_C_CHOWN         (0x13),
    BTRFS_SEND_C_UTIMES        (0x14),
    BTRFS_SEND_C_END           (0x15),
    BTRFS_SEND_C_UPDATE_EXTENT (0x16);

    private int value;

    BtrfsCommandType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
