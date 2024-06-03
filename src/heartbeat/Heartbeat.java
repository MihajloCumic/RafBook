package heartbeat;


import app.AppConfig;
import app.Cancellable;
import servent.message.HeartbeatRequestMessage;
import servent.message.util.MessageUtil;


public class Heartbeat implements Runnable, Cancellable {
    private volatile boolean working = true;
    private int nextNodePort;
    private volatile boolean hasResponded = false;
    private final int lowWaitingTime;

    public Heartbeat( int lowWaitingTime){
        this.lowWaitingTime = lowWaitingTime;
        this.nextNodePort = -1;

    }

    @Override
    public void stop() {
        working =false;
    }

    @Override
    public void run() {
        while(working){
            nextNodePort = AppConfig.chordState.getNextServentPort();
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

        if(!hasResponded){
            //poslati dodanty proveru
            AppConfig.timestampedStandardPrint("Nije odgovorio cvor: " + nextNodePort);
        }else{
            AppConfig.timestampedStandardPrint("Odgovorio je cvor.: " + nextNodePort);
            //restartovanje mape
           hasResponded = false;
        }

    }

    private void sendHeartbeat() throws InterruptedException {
            HeartbeatRequestMessage hbr = new HeartbeatRequestMessage(AppConfig.myServentInfo.getListenerPort(), nextNodePort);
            MessageUtil.sendMessage(hbr);

    }
}
