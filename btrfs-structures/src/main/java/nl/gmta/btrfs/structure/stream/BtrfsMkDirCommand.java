package nl.gmta.btrfs.structure.stream;

public class BtrfsMkDirCommand extends BtrfsInodeCommand {
    public BtrfsMkDirCommand(BtrfsCommandHeader header, String path, long inode) {
        super(header, path, inode);
    }
}
