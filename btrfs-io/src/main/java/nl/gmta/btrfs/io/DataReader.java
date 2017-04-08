package nl.gmta.btrfs.io;

import java.io.IOException;
import java.io.InputStream;

import nl.gmta.btrfs.io.exception.BtrfsStructureException;

class DataReader implements AutoCloseable {
    private final InputStream is;
    protected long position = 0;

    DataReader(InputStream is) {
        this.is = is;
    }

    int available() throws IOException {
        return this.is.available();
    }

    @Override
    public void close() throws IOException {
        this.is.close();
    }

    long readBE64() throws IOException {
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

    protected byte[] readBytes(int length) throws IOException {
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

    int readLE16() throws IOException {
        byte[] intBytes = this.readBytes(2);
        return (intBytes[0] & 0xFF)
            | ((intBytes[1] & 0xFF) << 8);
    }

    int readLE32() throws IOException {
        byte[] intBytes = this.readBytes(4);
        return (intBytes[0] & 0xFF)
            | ((intBytes[1] & 0xFF) << 8)
            | ((intBytes[2] & 0xFF) << 16)
            | ((intBytes[3] & 0xFF) << 24);
    }

    long readLE64() throws IOException {
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
