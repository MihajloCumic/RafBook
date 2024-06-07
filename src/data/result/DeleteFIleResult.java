package data.result;

public class DeleteFIleResult {
    private int resStatus;
    private String fileName;
    private int nodePort;

    public DeleteFIleResult(int resStatus, String fileName, int nodePort) {
        this.resStatus = resStatus;
        this.fileName = fileName;
        this.nodePort = nodePort;
    }

    public int getResStatus() {
        return resStatus;
    }

    public void setResStatus(int resStatus) {
        this.resStatus = resStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNodePort() {
        return nodePort;
    }

    public void setNodePort(int nodePort) {
        this.nodePort = nodePort;
    }
}
