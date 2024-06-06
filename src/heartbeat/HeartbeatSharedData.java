package heartbeat;

import app.AppConfig;
import app.ServentInfo;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HeartbeatSharedData {
    private static volatile HeartbeatSharedData instance;
    private final ConcurrentHashMap<Integer, Boolean> nodePortHasResponded;
    private int dataVersion = 0;


    private HeartbeatSharedData(){
        this.nodePortHasResponded = new ConcurrentHashMap<>();
    }


    public static HeartbeatSharedData getInstance(){
        HeartbeatSharedData result = instance;
        if(instance == null){
            synchronized (HeartbeatSharedData.class){
                result = instance;
                if(result == null){
                    result = instance = new HeartbeatSharedData();
                }
            }
        }
        return result;
    }

    public synchronized void refreshNodesToMonitor(){
        ServentInfo[] successors = AppConfig.chordState.getSuccessorTable();
        if(successors == null || successors.length == 0) return;
        nodePortHasResponded.clear();
        for (ServentInfo successor : successors) {
            if(successor == null) continue;
            nodePortHasResponded.put(successor.getListenerPort(), false);
        }
        dataVersion++;
    }

    public synchronized Set<Integer> nodePortsToMonitor(){
        return nodePortHasResponded.keySet();
    }

    public synchronized void hasResponded(Integer nodePort){
        if(nodePortHasResponded.containsKey(nodePort)){
            nodePortHasResponded.put(nodePort, true);
        }
    }

    public synchronized ConcurrentHashMap<Integer, Boolean> getNodePortHasResponded(){
        return nodePortHasResponded;
    }

    public synchronized int getDataVersion() {
        return dataVersion;
    }
}
