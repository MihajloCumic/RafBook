package data.file;

import data.enums.FileType;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MyFile {
    private String name;
    private byte[] content;
    private FileType fileType;

    public MyFile(String name, byte[] content, FileType fileType) {
        this.name = name;
        this.content = content;
        this.fileType = fileType;
    }
    public String getExtensionAsString(){
        if(fileType.equals(FileType.TYPE_TEXT)) return ".txt";
        if(fileType.equals(FileType.TYPE_JPG)) return ".jpg";
        if(fileType.equals(FileType.TYPE_PNG)) return ".png";
        return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "MyFile{" +
                "name='" + name + '\'' +
                ", content=" + new String(content, StandardCharsets.US_ASCII) +
                ", fileType=" + fileType +
                '}';
    }
}
