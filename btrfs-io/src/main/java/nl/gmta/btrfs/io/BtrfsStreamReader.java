package nl.gmta.btrfs.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import nl.gmta.btrfs.io.exception.BtrfsStructureException;
import nl.gmta.btrfs.structure.stream.BtrfsStreamElement;
import nl.gmta.btrfs.structure.stream.BtrfsStreamHeader;

public class BtrfsStreamReader implements AutoCloseable {
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

    private BtrfsStreamElement readElement() throws IOException {
        if (!this.isHeaderRead) {
            return this.readHeader();
        }

        throw new BtrfsStructureException("Unable to read more elements");
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

    private int readLE32() throws IOException {
        byte[] intBytes = this.readBytes(4);
        return (intBytes[3] << 24 | intBytes[2] << 16 | intBytes[1] << 8 | intBytes[0]);
    }
}
