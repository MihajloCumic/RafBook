package reader.impl;

import data.enums.FileType;
import data.file.MyFile;
import reader.Reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReader implements Reader {
    @Override
    public MyFile readFile(String location, String name) {
        Path path = Paths.get(location);
        if(isTextFile(path)){
            try {
                byte[] content = getFileContent(path);
                return new MyFile(name, content, FileType.TYPE_TEXT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(isImageFile(path)){
            try {
                byte[] content = getFileContent(path);
                return new MyFile(name, content, getImageFileType(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;

    }

    private boolean isTextFile(Path path){
        String fileName = path.getFileName().toString();
        return fileName.endsWith(".txt");

    }

    private boolean isImageFile(Path path){
        String fileName = path.getFileName().toString();
        return  fileName.endsWith(".jpg") || fileName.endsWith(".png");
    }

    private FileType getImageFileType(Path path){
        String fileName = path.getFileName().toString();
        if(fileName.endsWith(".jpg")) return FileType.TYPE_JPG;
        if (fileName.endsWith(".png")) return FileType.TYPE_PNG;
        return null;
    }

    private byte[] getFileContent(Path path) throws IOException {
        return Files.readAllBytes(path);
    }
}
