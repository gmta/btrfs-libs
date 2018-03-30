# btrfs-libs

Collection of Java structures and libraries for interacting with the btrfs filesystem.

## Current features

* Read btrfs streams
  * Supports all commands
  * CRC32C verification

## CI status
[![Build Status](https://travis-ci.org/GMTA/btrfs-libs.svg?branch=master)](https://travis-ci.org/GMTA/btrfs-libs)
[![Dependency Status](https://www.versioneye.com/user/projects/58e8c684d6c98d004652f41a/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/58e8c684d6c98d004652f41a)

## Example usage

### Reading a btrfs stream

```java
Path streamFile = Paths.get("/path/to/stream.dat");
try (InputStream is = new BufferedInputStream(Files.newInputStream(streamFile));
        BtrfsStreamReader reader = new BtrfsStreamReader(is)) {

    while (reader.hasNext()) {
        BtrfsStreamElement element = reader.next();
        System.out.println(element);
    }

} catch (IOException e) {
    e.printStackTrace();
}
```

Example output:

```
BtrfsStreamHeader{version=1}
BtrfsSnapshotCommand{header=BtrfsCommandHeader{length=79 command=SNAPSHOT crc=6ba462e6} path='my-snapshot' UUID=d671a994-8e64-c14b-b73d-6eee6fdf21db CTransID=1081204 cloneUUID=6e61cd26-0fe5-f84d-85df-8d6fb67f3cc8 cloneCTransID=1081185}
BtrfsWriteCommand{header=BtrfsCommandHeader{length=49185 command=WRITE crc=a5dcaab3} path='folder/file.dat' fileOffset=0 data=[49152 bytes]}
BtrfsUTimesCommand{header=BtrfsCommandHeader{length=65 command=UTIMES crc=b72bba65} path='folder/file.dat' atime=BtrfsTimespec{seconds=1521381730 nanoSeconds=631324600 : 2018-03-18T14:02:10.6313246Z} ctime=BtrfsTimespec{seconds=1522406824 nanoSeconds=308205750 : 2018-03-30T10:47:04.30820575Z} mtime=BtrfsTimespec{seconds=1522406824 nanoSeconds=308205750 : 2018-03-30T10:47:04.30820575Z}}
BtrfsChownCommand{header=BtrfsCommandHeader{length=94 command=CHOWN crc=af28f1c5} path='folder/file.dat' uid=8 gid=8}
BtrfsChmodCommand{header=BtrfsCommandHeader{length=82 command=CHMOD crc=f5e23d04} path='folder/file.dat' mode=660}
BtrfsEndCommand{header=BtrfsCommandHeader{length=0 command=END crc=9dc96c50}}
```