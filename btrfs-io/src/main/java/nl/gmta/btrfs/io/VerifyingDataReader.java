package nl.gmta.btrfs.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Checksum;

import nl.gmta.btrfs.io.exception.BtrfsStructureException;
import nl.gmta.linux.crypto.CRC32C;

/**
 * VerifyingDataReader behaves as a {@link DataReader} but understands the concept of on-demand
 * loading of command bodies and verifies both command length and data CRC.
 */
class VerifyingDataReader extends DataReader {
    private static final int SKIP_BUFFER_MAX_SIZE = 8192;

    private final Checksum checksum = new CRC32C();
    private boolean checksumZeroBytes = false;
    private long commandEndPosition;
    private long expectedChecksum;

    VerifyingDataReader(InputStream is) {
        super(is);
    }

    /**
     * Makes sure we've moved to the command's ending position so we can read the next command.
     */
    void ensureCommandFullyRead() throws IOException {
        // Read all data
        if (this.commandEndPosition > 0) {
            while (this.position < this.commandEndPosition) {
                int bufferSize = (int) Math.min(this.commandEndPosition - this.position, SKIP_BUFFER_MAX_SIZE);
                this.readBytes(bufferSize);
            }
        }

        // Reset checksum since we've finished reading a command
        this.checksum.reset();
    }

    @Override
    protected byte[] readBytes(int length) throws IOException {
        boolean verify = (this.commandEndPosition > 0);

        // Prevent reading beyond the command's end position
        if (verify && (this.position + length > this.commandEndPosition)) {
            throw new BtrfsStructureException("Reading beyond command end position is not allowed");
        }

        // Read actual bytes
        byte[] data = super.readBytes(length);

        // Update checksum with actual data or zeroes, depending on our current mode
        byte[] checksumData = this.checksumZeroBytes ? new byte[length] : data;
        this.checksum.update(checksumData, 0, length);

        // If we've reached the command's end position, verify the checksum
        if (verify && (this.position >= this.commandEndPosition)) {
            long checksumValue = this.checksum.getValue();
            if (checksumValue != this.expectedChecksum) {
                throw new BtrfsStructureException(String.format(
                    "Command body checksum mismatch: expected %08x but got %08x",
                    this.expectedChecksum,
                    checksumValue
                ));
            }

            // Reset verification
            this.commandEndPosition = 0;
        }

        return data;
    }

    /**
     * Temporarily disables passing the read bytes through our checksum. Used for reading the
     * CRC checksum in the btrfs command headers.
     *
     * @param checksumZeroBytes Whether zero bytes should be checksummed instead of the actual data
     */
    void setChecksumZeroBytes(boolean checksumZeroBytes) {
        this.checksumZeroBytes = checksumZeroBytes;
    }

    /**
     * Sets the expectations for the command next to read: command body size and CRC checksum.
     *
     * @param size The size of the command's body in bytes
     * @param crc32c The CRC32C checksum value of the command's body
     */
    void setCommandVerification(int size, long crc32c) {
        if (this.commandEndPosition > 0) {
            throw new IllegalStateException("Command verification cannot be set while a command is still in progress");
        }
        this.commandEndPosition = this.position + size;
        this.expectedChecksum = crc32c;
    }
}
