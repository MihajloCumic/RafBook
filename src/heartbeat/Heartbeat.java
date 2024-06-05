package heartbeat;


import app.AppConfig;
import app.Cancellable;
import servent.message.HeartbeatRequestMessage;
import servent.message.RecheckNodeMessage;
import servent.message.RemoveNodeMessage;
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
                if( nextNodePort != -1 && AppConfig.myServentInfo.getListenerPort() != nextNodePort){
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

            heartbeatSharedData.setIsSuspicious(true);

            synchronized (HeartbeatSharedData.getInstance()){

                if(HeartbeatSharedData.getInstance().getIsSuspicious()){

                    boolean isSent = sendRecheckMessage(nextNodePort);

                    try {
                        AppConfig.timestampedErrorPrint("waiting");
                        HeartbeatSharedData.getInstance().wait(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    removeNode(heartbeatSharedData);

                }
            }
        }else{
           // AppConfig.timestampedStandardPrint("Odgovorio je cvor.: " + nextNodePort);
           heartbeatSharedData.setHasResponded(false);
        }

    }

    private void removeNode(HeartbeatSharedData heartbeatSharedData){
        if(heartbeatSharedData.getIsSuspicious()){
            AppConfig.timestampedErrorPrint("mora se reaguje");
            int nodeToRemovePort = heartbeatSharedData.getServentPort();
            AppConfig.chordState.removeNode(heartbeatSharedData.getServentPort());
            heartbeatSharedData.setIsSuspicious(false);
            heartbeatSharedData.setHasResponded(false);
            heartbeatSharedData.setServentPOrt(-1);
            RemoveNodeMessage rnm = new RemoveNodeMessage(AppConfig.myServentInfo.getListenerPort(), AppConfig.chordState.getNextNodePort(), nodeToRemovePort + "");
            MessageUtil.sendMessage(rnm);
        }else{
            AppConfig.timestampedErrorPrint("Sve okej");
        }
    }

    private boolean sendRecheckMessage(int nextNodePort){
        int receiverNodePort = AppConfig.chordState.getRandomHealthyNodePort();
        if(receiverNodePort == -1){
            AppConfig.timestampedErrorPrint("No healthy nodes to send recheck message.");
            return false;
        }
        RecheckNodeMessage rnm = new RecheckNodeMessage(AppConfig.myServentInfo.getListenerPort(), receiverNodePort, nextNodePort + "");
        MessageUtil.sendMessage(rnm);
        return true;
    }

    private void sendHeartbeat() throws InterruptedException {
            HeartbeatRequestMessage hbr = new HeartbeatRequestMessage(AppConfig.myServentInfo.getListenerPort(), HeartbeatSharedData.getInstance().getServentPort());
            MessageUtil.sendMessage(hbr);

    }
}
