package data.result;

import data.file.MyFile;

import java.nio.charset.StandardCharsets;

public class GetResult {
    private int resStatus;
    private MyFile myFile;

    public GetResult(int resStatus) {
        this.resStatus = resStatus;
    }

    public GetResult(int resStatus, MyFile myFile) {
        this.resStatus = resStatus;
        this.myFile = myFile;
    }

    public GetResult(String stringRepresentation){
        String[] parts = stringRepresentation.split(":", 2);
        if(parts.length == 2){
            this.resStatus = Integer.parseInt(parts[0]);
            this.myFile = new MyFile(parts[1]);
            return;
        }
        if(parts.length == 1){
            this.resStatus = Integer.parseInt(parts[0]);
            return;
        }
        throw new IllegalArgumentException("Invalid argument.");
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

    @Override
    public String toString() {
        return resStatus + ":" + myFile.toString();
    }
}
