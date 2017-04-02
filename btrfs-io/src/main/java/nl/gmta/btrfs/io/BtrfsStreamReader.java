package nl.gmta.btrfs.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

import nl.gmta.btrfs.io.exception.BtrfsStructureException;
import nl.gmta.btrfs.structure.stream.BtrfsAttributeType;
import nl.gmta.btrfs.structure.stream.BtrfsCommandType;
import nl.gmta.btrfs.structure.stream.BtrfsSnapshotCommand;
import nl.gmta.btrfs.structure.stream.BtrfsStreamCommand;
import nl.gmta.btrfs.structure.stream.BtrfsStreamCommandHeader;
import nl.gmta.btrfs.structure.stream.BtrfsStreamElement;
import nl.gmta.btrfs.structure.stream.BtrfsStreamHeader;
import nl.gmta.btrfs.structure.stream.BtrfsTimespec;

public class BtrfsStreamReader implements AutoCloseable {
    private static final int VALUE_TYPE_TIMESPEC_SIZE = 12;
    private static final int VALUE_TYPE_U64_SIZE = 8;
    private static final int VALUE_TYPE_UUID_SIZE = 16;
    private static final int SUPPORTED_VERSION = 1;

    private final InputStream is;
    private boolean isHeaderRead = false;
    private long position = 0;

    public BtrfsStreamReader(InputStream is) {
        this.is = is;
    }

    @Override
    public void close() throws IOException {
        this.is.close();
    }

    public boolean hasNext() throws IOException {
        return (this.is.available() > 0);
    }

    public BtrfsStreamElement next() throws IOException {
        return this.readElement();
    }

    private Object readAttribute(BtrfsAttributeType type) throws IOException {
        // Read and verify attribute type
        int readType = this.readLE16();
        if (readType != type.getId()) {
            throw new BtrfsStructureException(String.format("Expected attribute type %04X but got %04X", type.getId(), readType));
        }

        // Read attribute value
        int length = this.readLE16();
        Object result;
        switch (type.getType()) {
            case BINARY:
                result = this.readBytes(length);
                break;
            case STRING:
                byte[] stringData = this.readBytes(length);
                result = new String(stringData, StandardCharsets.US_ASCII);
                break;
            case TIMESPEC:
                if (length != VALUE_TYPE_TIMESPEC_SIZE) {
                    throw new BtrfsStructureException(String.format("Unexpected timespec size: %d", length));
                }
                long seconds = this.readLE64();
                int nanoSeconds = this.readLE32();
                result = new BtrfsTimespec(seconds, nanoSeconds);
                break;
            case U64:
                if (length != VALUE_TYPE_U64_SIZE) {
                    throw new BtrfsStructureException(String.format("Unexpected U64 size: %d", length));
                }
                result = this.readLE64();
                break;
            case UUID:
                if (length != VALUE_TYPE_UUID_SIZE) {
                    throw new BtrfsStructureException(String.format("Unexpected UUID size: %d", length));
                }
                long mostSigUUID = this.readBE64();
                long leastSigUUID = this.readBE64();
                result = new UUID(mostSigUUID, leastSigUUID);
                break;
            default:
                throw new BtrfsStructureException(String.format("Unimplemented value type: %s", type.getType()));
        }
        return result;
    }

    private long readBE64() throws IOException {
        byte[] longBytes = this.readBytes(8);
        return (longBytes[7] & 0xFF)
            | ((longBytes[6] & 0xFF) << 8)
            | ((longBytes[5] & 0xFF) << 16)
            | ((long) (longBytes[4] & 0xFF) << 24)
            | ((long) (longBytes[3] & 0xFF) << 32)
            | ((long) (longBytes[2] & 0xFF) << 40)
            | ((long) (longBytes[1] & 0xFF) << 48)
            | ((long) (longBytes[0] & 0xFF) << 56);
    }

    private byte readByte() throws IOException {
        byte result = (byte) this.is.read();
        ++this.position;
        return result;
    }

    private byte[] readBytes(int length) throws IOException {
        byte[] buffer = new byte[length];
        int read = this.is.read(buffer);
        if (read < 0) {
            throw new BtrfsStructureException("Unexpected EOF while reading btrfs stream");
        } else if (read != length) {
            throw new BtrfsStructureException("Invalid number of bytes read");
        }

        this.position += read;
        return buffer;
    }

    private BtrfsStreamCommand readCommand() throws IOException {
        BtrfsStreamCommandHeader header = this.readCommandHeader();
        BtrfsStreamCommand command;

        // Read command body
        switch (header.getCommand()) {
            case SNAPSHOT:
                String path = (String) this.readAttribute(BtrfsAttributeType.PATH);
                UUID UUID = (UUID) this.readAttribute(BtrfsAttributeType.UUID);
                long CTransID = (Long) this.readAttribute(BtrfsAttributeType.CTRANSID);
                UUID cloneUUID = (UUID) this.readAttribute(BtrfsAttributeType.CLONE_UUID);
                long cloneCTransID = (Long) this.readAttribute(BtrfsAttributeType.CLONE_CTRANSID);

                command = new BtrfsSnapshotCommand(header, path, UUID, CTransID, cloneUUID, cloneCTransID);
                break;
            default:
                throw new BtrfsStructureException(String.format("Command not yet implemented: %s", header.getCommand()));
        }

        // TODO: header length check
        // TODO: CRC check

        return command;
    }

    private BtrfsStreamCommandHeader readCommandHeader() throws IOException {
        int length = this.readLE32();

        int commandValue = this.readLE16();
        BtrfsCommandType command = BtrfsCommandType.getById(commandValue);
        if (command == null) {
            throw new BtrfsStructureException(String.format("Invalid command type: %d", commandValue));
        }

        int crc = this.readLE32();

        return new BtrfsStreamCommandHeader(length, command, crc);
    }

    private BtrfsStreamElement readElement() throws IOException {
        if (!this.isHeaderRead) {
            return this.readHeader();
        } else {
            return this.readCommand();
        }
    }

    private BtrfsStreamHeader readHeader() throws IOException {
        // Read and verify magic (and NUL trail)
        byte[] magicExpect = BtrfsStreamHeader.MAGIC.getBytes(StandardCharsets.US_ASCII);
        byte[] magicActual = this.readBytes(magicExpect.length);
        byte nulTrail = this.readByte();
        if (!Arrays.equals(magicExpect, magicActual) || (nulTrail != 0)) {
            throw new BtrfsStructureException("No btrfs stream header magic found");
        }

        // Read and verify version
        int version = this.readLE32();
        if (version != SUPPORTED_VERSION) {
            throw new BtrfsStructureException(String.format("Unsupported btrfs stream version: %d", version));
        }

        this.isHeaderRead = true;
        return new BtrfsStreamHeader(version);
    }

    private int readLE16() throws IOException {
        byte[] intBytes = this.readBytes(2);
        return (intBytes[0] & 0xFF)
            | ((intBytes[1] & 0xFF) << 8);
    }

    private int readLE32() throws IOException {
        byte[] intBytes = this.readBytes(4);
        return (intBytes[0] & 0xFF)
            | ((intBytes[1] & 0xFF) << 8)
            | ((intBytes[2] & 0xFF) << 16)
            | ((intBytes[3] & 0xFF) << 24);
    }

    private long readLE64() throws IOException {
        byte[] longBytes = this.readBytes(8);
        return (longBytes[0] & 0xFF)
            | ((longBytes[1] & 0xFF) << 8)
            | ((longBytes[2] & 0xFF) << 16)
            | ((long) (longBytes[3] & 0xFF) << 24)
            | ((long) (longBytes[4] & 0xFF) << 32)
            | ((long) (longBytes[5] & 0xFF) << 40)
            | ((long) (longBytes[6] & 0xFF) << 48)
            | ((long) (longBytes[7] & 0xFF) << 56);
    }
}
