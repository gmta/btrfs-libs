package nl.gmta.btrfs.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

import nl.gmta.btrfs.io.exception.BtrfsStructureException;
import nl.gmta.btrfs.structure.stream.BtrfsAttributeType;
import nl.gmta.btrfs.structure.stream.BtrfsChmodCommand;
import nl.gmta.btrfs.structure.stream.BtrfsChownCommand;
import nl.gmta.btrfs.structure.stream.BtrfsCommandType;
import nl.gmta.btrfs.structure.stream.BtrfsEndCommand;
import nl.gmta.btrfs.structure.stream.BtrfsInodeCommand;
import nl.gmta.btrfs.structure.stream.BtrfsLinkCommand;
import nl.gmta.btrfs.structure.stream.BtrfsMkDirCommand;
import nl.gmta.btrfs.structure.stream.BtrfsMkFifoCommand;
import nl.gmta.btrfs.structure.stream.BtrfsMkFileCommand;
import nl.gmta.btrfs.structure.stream.BtrfsMkNodCommand;
import nl.gmta.btrfs.structure.stream.BtrfsMkSockCommand;
import nl.gmta.btrfs.structure.stream.BtrfsRenameCommand;
import nl.gmta.btrfs.structure.stream.BtrfsSnapshotCommand;
import nl.gmta.btrfs.structure.stream.BtrfsStreamCommand;
import nl.gmta.btrfs.structure.stream.BtrfsStreamCommandHeader;
import nl.gmta.btrfs.structure.stream.BtrfsStreamElement;
import nl.gmta.btrfs.structure.stream.BtrfsStreamHeader;
import nl.gmta.btrfs.structure.stream.BtrfsSymlinkCommand;
import nl.gmta.btrfs.structure.stream.BtrfsTimespec;
import nl.gmta.btrfs.structure.stream.BtrfsTruncateCommand;
import nl.gmta.btrfs.structure.stream.BtrfsUTimesCommand;
import nl.gmta.btrfs.structure.stream.BtrfsUnlinkCommand;
import nl.gmta.btrfs.structure.stream.BtrfsUpdateExtentCommand;
import nl.gmta.btrfs.structure.stream.BtrfsWriteCommand;

public class BtrfsStreamReader implements AutoCloseable {
    private static final int VALUE_TYPE_TIMESPEC_SIZE = 12;
    private static final int VALUE_TYPE_U64_SIZE = 8;
    private static final int VALUE_TYPE_UUID_SIZE = 16;
    private static final int SUPPORTED_VERSION = 1;

    private final VerifyingDataReader reader;
    private boolean isHeaderRead = false;

    public BtrfsStreamReader(InputStream is) {
        this.reader = new VerifyingDataReader(is);
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }

    public boolean hasNext() throws IOException {
        return (this.reader.available() > 0);
    }

    public BtrfsStreamElement next() throws IOException {
        return this.readElement();
    }

    private Object readAttribute(BtrfsAttributeType type) throws IOException {
        // Read and verify attribute type
        int readType = this.reader.readLE16();
        if (readType != type.getId()) {
            throw new BtrfsStructureException(String.format("Expected attribute type %04X but got %04X", type.getId(), readType));
        }

        // Read attribute value
        int length = this.reader.readLE16();
        Object result;
        switch (type.getType()) {
            case BINARY:
                result = this.reader.readBytes(length);
                break;
            case STRING:
                byte[] stringData = this.reader.readBytes(length);
                result = new String(stringData, StandardCharsets.US_ASCII);
                break;
            case TIMESPEC:
                if (length != VALUE_TYPE_TIMESPEC_SIZE) {
                    throw new BtrfsStructureException(String.format("Unexpected timespec size: %d", length));
                }
                long seconds = this.reader.readLE64();
                int nanoSeconds = this.reader.readLE32();
                result = new BtrfsTimespec(seconds, nanoSeconds);
                break;
            case U64:
                if (length != VALUE_TYPE_U64_SIZE) {
                    throw new BtrfsStructureException(String.format("Unexpected U64 size: %d", length));
                }
                result = this.reader.readLE64();
                break;
            case UUID:
                if (length != VALUE_TYPE_UUID_SIZE) {
                    throw new BtrfsStructureException(String.format("Unexpected UUID size: %d", length));
                }
                long mostSigUUID = this.reader.readBE64();
                long leastSigUUID = this.reader.readBE64();
                result = new UUID(mostSigUUID, leastSigUUID);
                break;
            default:
                throw new BtrfsStructureException(String.format("Unimplemented value type: %s", type.getType()));
        }
        return result;
    }

    private BtrfsStreamCommand readCommand() throws IOException {
        // Make sure we fully read and verified the previous command
        this.reader.ensureCommandFullyRead();

        // Read command header
        BtrfsStreamCommandHeader header = this.readCommandHeader();
        this.reader.setCommandVerification(header.getLength(), header.getCrc());

        // Construct command
        switch (header.getCommand()) {
            case CHMOD:
                return this.readChmodCommand(header);
            case CHOWN:
                return this.readChownCommand(header);
            case END:
                return this.readEndCommand(header);
            case LINK:
                return this.readLinkCommand(header);
            case RENAME:
                return this.readRenameCommand(header);
            case SNAPSHOT:
                return this.readSnapshotCommand(header);
            case TRUNCATE:
                return this.readTruncateCommand(header);
            case UPDATE_EXTENT:
                return this.readUpdateExtentCommand(header);
            case UNLINK:
                return this.readUnlinkCommand(header);
            case UTIMES:
                return this.readUTimesCommand(header);
            case WRITE:
                return this.readWriteCommand(header);
            case MKDIR:
            case MKFIFO:
            case MKFILE:
            case MKNOD:
            case MKSOCK:
            case SYMLINK:
                return this.readInodeCommand(header);
            default:
                throw new BtrfsStructureException(String.format("Command not yet implemented: %s", header.getCommand()));
        }
    }

    private BtrfsStreamCommandHeader readCommandHeader() throws IOException {
        // Total length of command body after the header
        int length = this.reader.readLE32();

        // The command type
        int commandValue = this.reader.readLE16();
        BtrfsCommandType command = BtrfsCommandType.getById(commandValue);
        if (command == null) {
            throw new BtrfsStructureException(String.format("Invalid command type: %d", commandValue));
        }

        // The CRC32 checksum of the header + body (w/ zeroes for the checksum value itself)
        this.reader.setChecksumZeroBytes(true);
        long crc = this.reader.readLE32() & 0xFFFFFFFFL;
        this.reader.setChecksumZeroBytes(false);

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
        byte[] magicActual = this.reader.readBytes(magicExpect.length);
        byte nulTrail = this.reader.readBytes(1)[0];
        if (!Arrays.equals(magicExpect, magicActual) || (nulTrail != 0)) {
            throw new BtrfsStructureException("No btrfs stream header magic found");
        }

        // Read and verify version
        int version = this.reader.readLE32();
        if (version != SUPPORTED_VERSION) {
            throw new BtrfsStructureException(String.format("Unsupported btrfs stream version: %d", version));
        }

        this.isHeaderRead = true;
        return new BtrfsStreamHeader(version);
    }

    private BtrfsChmodCommand readChmodCommand(BtrfsStreamCommandHeader header) throws IOException {
        String path = (String) this.readAttribute(BtrfsAttributeType.PATH);
        long mode = (Long) this.readAttribute(BtrfsAttributeType.MODE);

        return new BtrfsChmodCommand(header, path, mode);
    }

    private BtrfsChownCommand readChownCommand(BtrfsStreamCommandHeader header) throws IOException {
        String path = (String) this.readAttribute(BtrfsAttributeType.PATH);
        long uid = (Long) this.readAttribute(BtrfsAttributeType.UID);
        long gid = (Long) this.readAttribute(BtrfsAttributeType.GID);

        return new BtrfsChownCommand(header, path, uid, gid);
    }

    private BtrfsEndCommand readEndCommand(BtrfsStreamCommandHeader header) throws IOException {
        return new BtrfsEndCommand(header);
    }

    private BtrfsInodeCommand readInodeCommand(BtrfsStreamCommandHeader header) throws IOException {
        String path = (String) this.readAttribute(BtrfsAttributeType.PATH);
        long inode = (Long) this.readAttribute(BtrfsAttributeType.INO);

        switch (header.getCommand()) {
            case MKFILE:
                return new BtrfsMkFileCommand(header, path, inode);
            case MKDIR:
                return new BtrfsMkDirCommand(header, path, inode);
            case SYMLINK:
                String link = (String) this.readAttribute(BtrfsAttributeType.PATH_LINK);
                return new BtrfsSymlinkCommand(header, path, inode, link);
            default:
                // Do nothing, handled below
        }

        long rdev = (Long) this.readAttribute(BtrfsAttributeType.RDEV);
        long mode = (Long) this.readAttribute(BtrfsAttributeType.MODE);

        switch (header.getCommand()) {
            case MKFIFO:
                return new BtrfsMkFifoCommand(header, path, inode, rdev, mode);
            case MKNOD:
                return new BtrfsMkNodCommand(header, path, inode, rdev, mode);
            case MKSOCK:
                return new BtrfsMkSockCommand(header, path, inode, rdev, mode);
            default:
                throw new BtrfsStructureException(String.format("Invalid inode command: %s", header.getCommand()));
        }
    }

    private BtrfsLinkCommand readLinkCommand(BtrfsStreamCommandHeader header) throws IOException {
        String path = (String) this.readAttribute(BtrfsAttributeType.PATH);
        String link = (String) this.readAttribute(BtrfsAttributeType.PATH_LINK);

        return new BtrfsLinkCommand(header, path, link);
    }

    private BtrfsRenameCommand readRenameCommand(BtrfsStreamCommandHeader header) throws IOException {
        String path = (String) this.readAttribute(BtrfsAttributeType.PATH);
        String to = (String) this.readAttribute(BtrfsAttributeType.PATH_TO);

        return new BtrfsRenameCommand(header, path, to);
    }

    private BtrfsSnapshotCommand readSnapshotCommand(BtrfsStreamCommandHeader header) throws IOException {
        String path = (String) this.readAttribute(BtrfsAttributeType.PATH);
        UUID UUID = (UUID) this.readAttribute(BtrfsAttributeType.UUID);
        long CTransID = (Long) this.readAttribute(BtrfsAttributeType.CTRANSID);
        UUID cloneUUID = (UUID) this.readAttribute(BtrfsAttributeType.CLONE_UUID);
        long cloneCTransID = (Long) this.readAttribute(BtrfsAttributeType.CLONE_CTRANSID);

        return new BtrfsSnapshotCommand(header, path, UUID, CTransID, cloneUUID, cloneCTransID);
    }

    private BtrfsTruncateCommand readTruncateCommand(BtrfsStreamCommandHeader header) throws IOException {
        String path = (String) this.readAttribute(BtrfsAttributeType.PATH);
        long size = (Long) this.readAttribute(BtrfsAttributeType.SIZE);

        return new BtrfsTruncateCommand(header, path, size);
    }

    private BtrfsUpdateExtentCommand readUpdateExtentCommand(BtrfsStreamCommandHeader header) throws IOException {
        String path = (String) this.readAttribute(BtrfsAttributeType.PATH);
        long fileOffset = (Long) this.readAttribute(BtrfsAttributeType.FILE_OFFSET);
        long size = (Long) this.readAttribute(BtrfsAttributeType.SIZE);

        return new BtrfsUpdateExtentCommand(header, path, fileOffset, size);
    }

    private BtrfsUnlinkCommand readUnlinkCommand(BtrfsStreamCommandHeader header) throws IOException {
        String path = (String) this.readAttribute(BtrfsAttributeType.PATH);

        return new BtrfsUnlinkCommand(header, path);
    }

    private BtrfsUTimesCommand readUTimesCommand(BtrfsStreamCommandHeader header) throws IOException {
        String path = (String) this.readAttribute(BtrfsAttributeType.PATH);
        BtrfsTimespec atime = (BtrfsTimespec) this.readAttribute(BtrfsAttributeType.ATIME);
        BtrfsTimespec mtime = (BtrfsTimespec) this.readAttribute(BtrfsAttributeType.MTIME);
        BtrfsTimespec ctime = (BtrfsTimespec) this.readAttribute(BtrfsAttributeType.CTIME);

        return new BtrfsUTimesCommand(header, path, atime, mtime, ctime);
    }

    @SuppressWarnings("resource")
    private BtrfsWriteCommand readWriteCommand(BtrfsStreamCommandHeader header) throws IOException {
        long fieldsStartPosition = this.reader.getPosition();

        String path = (String) this.readAttribute(BtrfsAttributeType.PATH);
        long fileOffset = (Long) this.readAttribute(BtrfsAttributeType.FILE_OFFSET);

        long fieldsEndPosition = this.reader.getPosition();
        int fieldsSize = (int) (fieldsEndPosition - fieldsStartPosition);
        int dataSize = header.getLength() - fieldsSize;

        ReadableByteChannel data = new SizedBinaryReader(this.reader, dataSize);

        return new BtrfsWriteCommand(header, path, fileOffset, dataSize, data);
    }
}
