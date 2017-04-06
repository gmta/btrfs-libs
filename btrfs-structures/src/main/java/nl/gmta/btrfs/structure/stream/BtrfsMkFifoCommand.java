package nl.gmta.btrfs.structure.stream;

public class BtrfsMkFifoCommand extends BtrfsDeviceCommand {
    public BtrfsMkFifoCommand(BtrfsStreamCommandHeader header, String path, long inode, long rdev, long mode) {
        super(header, path, inode, rdev, mode);
    }
}
