package data.file;

import data.enums.FileType;
import java.io.Serializable;
import java.util.Arrays;

public class MyFile implements Serializable {
    private static final long serialVersionUID = 1L;
    private byte[] content;
    private String name;
    private FileType fileType;
    private boolean isPrivate;

    public MyFile(byte[] content, String name, FileType fileType, boolean isPrivate) {
        this.content = content;
        this.name = name;
        this.fileType = fileType;
        this.isPrivate = isPrivate;
    }

    public String getExtensionAsString(){
        if(fileType ==  FileType.TYPE_TEXT) return ".txt";
        if(fileType ==  FileType.TYPE_JPG) return ".jpg";
        if(fileType ==  FileType.TYPE_PNG) return ".png";
        throw new RuntimeException("No extension found");
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "MyFile{" +
                "content=" + Arrays.toString(content) +
                ", name='" + name + '\'' +
                ", fileType=" + fileType +
                ", isPrivate=" + isPrivate +
                '}';
    }
}
