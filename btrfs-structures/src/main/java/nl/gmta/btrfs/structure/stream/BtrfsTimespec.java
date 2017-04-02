package nl.gmta.btrfs.structure.stream;

public class BtrfsTimespec {
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

    @Override
    public String toString() {
        return String.format("%s{seconds=%d nanoSeconds=%d}", this.getClass().getSimpleName(), this.seconds, this.nanoSeconds);
    }
}
