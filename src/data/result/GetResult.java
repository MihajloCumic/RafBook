package data.result;

import data.file.MyFile;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class GetResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private int resStatus;
    private MyFile myFile;

    public GetResult(int resStatus, MyFile myFile) {
        this.resStatus = resStatus;
        this.myFile = myFile;
    }

    public int getResStatus() {
        return resStatus;
    }

    public void setResStatus(int resStatus) {
        this.resStatus = resStatus;
    }

    public MyFile getMyFile() {
        return myFile;
    }

    public void setMyFile(MyFile myFile) {
        this.myFile = myFile;
    }
}
