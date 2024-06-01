package data.file;

import app.AppConfig;
import data.enums.FileType;

import java.nio.charset.StandardCharsets;

public class MyFile {

    private byte[] content;
    private FileType fileType;
    private boolean isPrivate;

    public MyFile(byte[] content, FileType fileType, boolean isPrivate) {
        this.content = content;
        this.fileType = fileType;
        this.isPrivate = isPrivate;
    }

    public MyFile(String stringRepresentation) throws IllegalArgumentException{
        String[] parts = stringRepresentation.split("\uFFFF", 3);
        if(parts.length != 3) throw new IllegalArgumentException("Invalid argument.");
        this.content = parts[0].getBytes(StandardCharsets.US_ASCII);
        this.fileType = getTypeFromString(parts[1]);
        this.isPrivate = parts[2].equals("true");
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public String toString() {
        String asciiContent = new String(content, StandardCharsets.US_ASCII);
        return asciiContent + "\uFFFF" + getExtensionAsString() + "\uFFFF" + isPrivate;
    }
}
