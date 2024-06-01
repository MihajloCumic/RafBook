package data.file;

import app.AppConfig;
import data.enums.FileType;

import java.nio.charset.StandardCharsets;

public class MyFile {

    private byte[] content;
    private FileType fileType;

    public MyFile(byte[] content, FileType fileType) {
        this.content = content;
        this.fileType = fileType;
    }

    public MyFile(String stringRepresentation) throws IllegalArgumentException{
        AppConfig.timestampedErrorPrint("usao");
        String[] parts = stringRepresentation.split("\uFFFF", 2);
        AppConfig.timestampedErrorPrint(parts[0]);
        AppConfig.timestampedErrorPrint(parts[1]);
        if(parts.length != 2) throw new IllegalArgumentException("Invalid argument.");
        AppConfig.timestampedErrorPrint("prosao bez problema if");
        this.content = parts[0].getBytes(StandardCharsets.US_ASCII);
        this.fileType = getTypeFromString(parts[1]);
    }

    public String getExtensionAsString(){
        if(fileType.equals(FileType.TYPE_TEXT)) return ".txt";
        if(fileType.equals(FileType.TYPE_JPG)) return ".jpg";
        if(fileType.equals(FileType.TYPE_PNG)) return ".png";
        return "";
    }

    private FileType getTypeFromString(String extension){
        if(extension.equals(".txt")) return FileType.TYPE_TEXT;
        if(extension.equals(".jpg")) return FileType.TYPE_JPG;
        if(extension.equals(".png")) return FileType.TYPE_PNG;
        throw new IllegalArgumentException("No such extension");
    }


    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        String asciiContent = new String(content, StandardCharsets.US_ASCII);
        return asciiContent + "\uFFFF" + getExtensionAsString();
    }
}
