package nl.gmta.btrfs.structure.stream;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class BtrfsTimespec {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    private final long seconds;
    private final int nanoSeconds;

    public BtrfsTimespec(long seconds, int nanoSeconds) {
        this.seconds = seconds;
        this.nanoSeconds = nanoSeconds;
    }

    public long getSeconds() {
        return this.seconds;
    }

    public long getNanoSeconds() {
        return this.nanoSeconds;
    }

    public OffsetDateTime getTimestamp() {
        return Instant.ofEpochSecond(this.seconds, this.nanoSeconds).atOffset(ZoneOffset.UTC);
    }

    @Override
    public String toString() {
        return String.format(
            "%s{seconds=%d nanoSeconds=%d : %s}",
            this.getClass().getSimpleName(),
            this.seconds,
            this.nanoSeconds,
            formatter.format(this.getTimestamp())
        );
    }
}
