package writer;

import data.file.MyFile;

public interface Writer {
    void saveFile(String location, String fileName, MyFile myFile);
    void saveFile(String location, MyFile myFile);
}
