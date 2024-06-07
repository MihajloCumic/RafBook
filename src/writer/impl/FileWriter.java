package writer.impl;

import app.AppConfig;
import data.file.MyFile;
import writer.Writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWriter implements Writer {
    @Override
    public void saveFile(String location, String fileName, MyFile myFile) {
        byte[] bytes = myFile.getContent();
        try {
            Path path = Paths.get(location  + fileName + myFile.getExtensionAsString());
            Files.write(path, bytes);
        } catch (IOException e) {
            AppConfig.timestampedStandardPrint("Could not save file: " + fileName);
        }

    }

    @Override
    public void saveFile(String location, MyFile myFile) {
        byte[] bytes = myFile.getContent();
        try {
            Path path = Paths.get(location  + "/" + myFile.getName());
            Files.write(path, bytes);
        } catch (IOException e) {
            AppConfig.timestampedStandardPrint("Could not save file: " + myFile.getName());
        }
    }
}
