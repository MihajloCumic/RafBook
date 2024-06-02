package heartbeat;


import app.AppConfig;
import app.Cancellable;
import app.ServentInfo;
import servent.message.HeartbeatRequestMessage;
import servent.message.util.MessageUtil;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class Heartbeat implements Runnable, Cancellable {
    private volatile boolean working = true;
    private final List<ServentInfo> servents;
    private final Map<Integer, Boolean> toMonitorMap;
    private final int lowWaitingTime;

    public Heartbeat( int lowWaitingTime){
        this.toMonitorMap = new ConcurrentHashMap<>();
        this.servents = new CopyOnWriteArrayList<>();
        this.lowWaitingTime = lowWaitingTime;

    }

    @Override
    public void stop() {
        working =false;
    }

    @Override
    public void run() {
        while(working){
            try {
                if(!servents.isEmpty()){
                    sendHeartbeats();
                    wait(lowWaitingTime);
                    checkHealth();
                }
                wait(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void checkHealth(){
        for(ServentInfo servent: servents){
            int chordId = servent.getChordId();
            if(toMonitorMap.containsKey(chordId)){
                if(!toMonitorMap.get(chordId)){
                    //poslati dodanty proveru
                    AppConfig.timestampedStandardPrint("Nije odgovorio cvor: " + servent.getListenerPort());
                }else{
                    //restartovanje mape
                    toMonitorMap.put(chordId, false);
                }
            }
        }

    }

    private void sendHeartbeats() throws InterruptedException {
        for(ServentInfo servent: servents){
            HeartbeatRequestMessage hbr = new HeartbeatRequestMessage(AppConfig.myServentInfo.getListenerPort(), servent.getListenerPort());
            MessageUtil.sendMessage(hbr);
            toMonitorMap.put(servent.getChordId(), false);
        }
    }
}
