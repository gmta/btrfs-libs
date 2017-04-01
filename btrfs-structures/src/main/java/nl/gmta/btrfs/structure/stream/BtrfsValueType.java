package nl.gmta.btrfs.structure.stream;

enum BtrfsValueType {
    U8       (0x00),
    U16      (0x01),
    U32      (0x02),
    U64      (0x03),
    BINARY   (0x04),
    STRING   (0x05),
    UUID     (0x06),
    TIMESPEC (0x07);

    private int value;

    BtrfsValueType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
