package reader;

import data.file.MyFile;

public interface Reader {
    MyFile readFile(String location, String name);
}
