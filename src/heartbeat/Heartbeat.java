package heartbeat;


import app.AppConfig;
import app.Cancellable;
import servent.message.HeartbeatRequestMessage;
import servent.message.RecheckNodeMessage;
import servent.message.RemoveNodeMessage;
import servent.message.util.MessageUtil;

import java.util.*;


public class Heartbeat implements Runnable, Cancellable {
    private volatile boolean working = true;
    private final int lowWaitingTime;
    private final int highWaitingTime = 10000;
    private final int heartbeatCycleInterval = 10000;

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
            HeartbeatSharedData sharedData = HeartbeatSharedData.getInstance();
            Set<Integer> nodePortsToMonitor = sharedData.nodePortsToMonitor();
            int dataVersion = sharedData.getDataVersion();
            sendHeartbeats(nodePortsToMonitor);
            try {
                Thread.sleep(lowWaitingTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Map<Integer, Boolean> nodesResponded = sharedData.getNodePortHasResponded();
            boolean waitForRechecks = recheckNotRespondedNodes(nodesResponded);
            if(waitForRechecks){
                AppConfig.timestampedErrorPrint("Waiting for rechecks.");
                try {
                    Thread.sleep(highWaitingTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if(sharedData.getDataVersion() == dataVersion){
                removeNodes(sharedData.getNodePortHasResponded());
            }else {
                AppConfig.timestampedErrorPrint("Changed version from: " + dataVersion + ", to: " + sharedData.getDataVersion());
            }
            sharedData.refreshNodesToMonitor();
            try {
                Thread.sleep(heartbeatCycleInterval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //dht_put public 3 image2.jpg
    private void sendHeartbeats(Set<Integer> nodePortsToMonitor){
        for(Integer port: nodePortsToMonitor){
            HeartbeatRequestMessage hrm = new HeartbeatRequestMessage(AppConfig.myServentInfo.getListenerPort(), port);
            MessageUtil.sendMessage(hrm);
        }
    }

    private boolean recheckNotRespondedNodes(Map<Integer, Boolean> nodesRespondedMap){
        List<Integer> notRespondedNodePorts = new ArrayList<>();
        List<Integer> respondedNodePorts = new ArrayList<>();
        for(Map.Entry<Integer, Boolean> entry: nodesRespondedMap.entrySet()){
            if(entry.getValue()) respondedNodePorts.add(entry.getKey());
            else notRespondedNodePorts.add(entry.getKey());
        }
        if(respondedNodePorts.isEmpty()){
            //remove all
            return false;
        }
        if(notRespondedNodePorts.isEmpty()){
            return false;
        }
        for(Integer suspicious: notRespondedNodePorts){
            int healthyNodePort = getRandomHealthyNodePort(respondedNodePorts);
            RecheckNodeMessage rnm = new RecheckNodeMessage(AppConfig.myServentInfo.getListenerPort(), healthyNodePort, suspicious + "");
            MessageUtil.sendMessage(rnm);
        }
        return true;
    }

    private void removeNodes(Map<Integer, Boolean> allMonitoredNodes){
        List<Integer> toRemoveNodePorts = new ArrayList<>();
        List<Integer> healthyNodePorts = new ArrayList<>();
        for(Map.Entry<Integer, Boolean> entry: allMonitoredNodes.entrySet()){
            if(entry.getValue()) healthyNodePorts.add(entry.getKey());
            else toRemoveNodePorts.add(entry.getKey());
        }
        for (Integer nodeToRemove: toRemoveNodePorts){
            AppConfig.chordState.removeNode(nodeToRemove);
            if(healthyNodePorts.isEmpty()){
                continue;
            }
            RemoveNodeMessage rnm = new RemoveNodeMessage(AppConfig.myServentInfo.getListenerPort(), getRandomHealthyNodePort(healthyNodePorts), nodeToRemove+"");
            MessageUtil.sendMessage(rnm);
        }
    }

    private int getRandomHealthyNodePort(List<Integer> healthyNodes){
        Random random = new Random();
        int randomIndex = random.nextInt(healthyNodes.size());
        return healthyNodes.get(randomIndex);
    }

}
