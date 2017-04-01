package nl.gmta.btrfs.structure.stream;

enum BtrfsValueType {
    BTRFS_TLV_U8       (0x00),
    BTRFS_TLV_U16      (0x01),
    BTRFS_TLV_U32      (0x02),
    BTRFS_TLV_U64      (0x03),
    BTRFS_TLV_BINARY   (0x04),
    BTRFS_TLV_STRING   (0x05),
    BTRFS_TLV_UUID     (0x06),
    BTRFS_TLV_TIMESPEC (0x07);

    private int value;

    BtrfsValueType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
