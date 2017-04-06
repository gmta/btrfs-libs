package nl.gmta.btrfs.structure.stream;

public class BtrfsMkSockCommand extends BtrfsDeviceCommand {
    public BtrfsMkSockCommand(BtrfsStreamCommandHeader header, String path, long inode, long rdev, long mode) {
        super(header, path, inode, rdev, mode);
    }
}
