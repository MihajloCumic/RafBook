package data.backup;


import data.enums.FileType;

public class Backup {
    private int chordId;
    private String location;
    private String name;
    private boolean isPrivate;
    private FileType fileType;

    public Backup(String location, String name, boolean isPrivate, FileType fileType, int chordId) {
        this.location = location;
        this.name = name;
        this.isPrivate = isPrivate;
        this.fileType = fileType;
        this.chordId = chordId;
    }

    public int getChordId() {
        return chordId;
    }

    public void setChordId(int chordId) {
        this.chordId = chordId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
