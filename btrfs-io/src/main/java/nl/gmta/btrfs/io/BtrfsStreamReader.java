package nl.gmta.btrfs.io;

import java.io.InputStream;
import java.util.Iterator;

import nl.gmta.btrfs.structure.stream.BtrfsStreamElement;

public class BtrfsStreamReader implements AutoCloseable, Iterator<BtrfsStreamElement> {
    private final InputStream is;

    public BtrfsStreamReader(InputStream is) {
        this.is = is;
    }

    @Override
    public void close() throws Exception {
        this.is.close();
    }

    @Override
    public boolean hasNext() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public BtrfsStreamElement next() {
        // TODO Auto-generated method stub
        return null;
    }
}
