package nl.gmta.btrfs.io.exception;

public class BtrfsStructureException extends RuntimeException {
    private static final long serialVersionUID = -6382833512451101912L;

    public BtrfsStructureException(String message) {
        super(message);
    }
}
