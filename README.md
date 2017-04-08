# btrfs-libs [![Build Status](https://travis-ci.org/GMTA/btrfs-libs.svg?branch=master)](https://travis-ci.org/GMTA/btrfs-libs)

Collection of Java structures and libraries for interacting with the btrfs filesystem.

## Current features

* Read btrfs streams
  * Supports all commands
  * CRC32C verification

## Example usage

### Reading a btrfs stream

```java
Path streamFile = Paths.get("/path/to/stream.dat");
try (InputStream is = Files.newInputStream(streamFile);
        BufferedInputStream bis = new BufferedInputStream(is);
        BtrfsStreamReader reader = new BtrfsStreamReader(bis)) {

    while (reader.hasNext()) {
        BtrfsStreamElement element = reader.next();
        System.out.println(element);
    }

} catch (IOException e) {
    e.printStackTrace();
}
```
