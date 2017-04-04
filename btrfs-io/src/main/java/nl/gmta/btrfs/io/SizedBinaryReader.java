package nl.gmta.btrfs.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;

public class SizedBinaryReader implements ReadableByteChannel {
    private final DataReader reader;
    private final int size;
    private boolean isOpen;
    private long readerPosition;
    private int position;

    public SizedBinaryReader(DataReader reader, int size) {
        this.reader = reader;
        this.size = size;

        this.isOpen = true;
        this.readerPosition = reader.getPosition();
        this.position = 0;
    }

    @Override
    public boolean isOpen() {
        return this.isOpen;
    }

    @Override
    public void close() throws IOException {
        this.isOpen = false;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        if (!this.isOpen) {
            throw new ClosedChannelException();
        } else if (this.readerPosition != this.reader.getPosition()) {
            throw new IOException("Cannot read data since reader position is altered since last read");
        }

        // Copy data to the provided buffer
        int copyBytes = Math.min(dst.remaining(), this.size - this.position);
        byte[] buffer = this.reader.readBytes(copyBytes);
        dst.put(buffer);

        this.position += buffer.length;
        this.readerPosition = this.reader.getPosition();

        return buffer.length;
    }
}
