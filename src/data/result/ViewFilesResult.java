package data.result;

import java.io.Serializable;

public class ViewFilesResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private int chordId;
    private String name;

    public int getChordId() {
        return chordId;
    }

    public void setChordId(int chordId) {
        this.chordId = chordId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ViewFilesResult(int chordId, String name) {
        this.chordId = chordId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ViewFilesResult{" +
                "chordId=" + chordId +
                ", name='" + name + '\'' +
                '}';
    }
}
