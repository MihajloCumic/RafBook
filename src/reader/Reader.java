package reader;

import data.file.MyFile;

import java.nio.file.Path;

public interface Reader {
    MyFile readFile(Path path);
}
