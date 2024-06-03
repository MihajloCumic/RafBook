package heartbeat;


import app.AppConfig;
import app.Cancellable;
import servent.message.HeartbeatRequestMessage;
import servent.message.util.MessageUtil;


public class Heartbeat implements Runnable, Cancellable {
    private volatile boolean working = true;
    private final int lowWaitingTime;

    public Heartbeat( int lowWaitingTime){
        this.lowWaitingTime = lowWaitingTime;

    }

    @Override
    public void stop() {
        working =false;
    }

    @Override
    public void run() {
        while(working){
            int nextNodePort = HeartbeatSharedData.getInstance().getServentPort();
            try {
                if( nextNodePort != -1){
                    sendHeartbeat();
                    Thread.sleep(lowWaitingTime);
                    checkHealth();
                }
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void checkHealth(){
        HeartbeatSharedData heartbeatSharedData = HeartbeatSharedData.getInstance();
        boolean hasResponded = heartbeatSharedData.getHasResponded();
        int nextNodePort = heartbeatSharedData.getServentPort();
        if(!hasResponded){
            AppConfig.timestampedStandardPrint("Nije odgovorio cvor: " + nextNodePort);
        }else{
            AppConfig.timestampedStandardPrint("Odgovorio je cvor.: " + nextNodePort);
           heartbeatSharedData.setHasResponded(false);
        }

    }

    private void sendHeartbeat() throws InterruptedException {
            HeartbeatRequestMessage hbr = new HeartbeatRequestMessage(AppConfig.myServentInfo.getListenerPort(), HeartbeatSharedData.getInstance().getServentPort());
            MessageUtil.sendMessage(hbr);

    }
}
