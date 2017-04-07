package nl.gmta.btrfs.structure.stream;

public class BtrfsMkFileCommand extends BtrfsInodeCommand {
    public BtrfsMkFileCommand(BtrfsCommandHeader header, String path, long inode) {
        super(header, path, inode);
    }
}
