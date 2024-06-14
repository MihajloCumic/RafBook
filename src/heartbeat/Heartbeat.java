package heartbeat;


import app.AppConfig;
import app.Cancellable;
import app.ChordState;
import data.backup.Backup;
import data.file.MyFile;
import servent.message.HeartbeatRequestMessage;
import servent.message.LoadBackupsMessage;
import servent.message.RecheckNodeMessage;
import servent.message.RemoveNodeMessage;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.MappedByteBuffer;
import java.util.*;


public class Heartbeat implements Runnable, Cancellable {
    private volatile boolean working = true;
    private final int lowWaitingTime;
    private final int highWaitingTime;
    private final int heartbeatCycleInterval = 10000;

    public Heartbeat( int lowWaitingTime, int highWaitingTime){
        this.lowWaitingTime = lowWaitingTime;
        this.highWaitingTime = highWaitingTime;

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
            if(nodePortsToMonitor.isEmpty()){
                try {
                    Thread.sleep(heartbeatCycleInterval);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
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
            else {
                notRespondedNodePorts.add(entry.getKey());
                AppConfig.chordState.setIsSuspiciousByPort(entry.getKey());
            }
        }
        if(notRespondedNodePorts.isEmpty()){
            return false;
        }
        if(respondedNodePorts.isEmpty()){
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
        List<Integer> healthyNodePorts  = AppConfig.chordState.getAllHealthyNodes();
        for(Map.Entry<Integer, Boolean> entry: allMonitoredNodes.entrySet()){
            if(entry.getValue()) continue;
            else toRemoveNodePorts.add(entry.getKey());
        }
        if(toRemoveNodePorts.isEmpty()) return;
        if(healthyNodePorts.isEmpty()){
            lastNodeAction(toRemoveNodePorts);
            return;
        }
        for (Integer nodeToRemove: toRemoveNodePorts){
            AppConfig.chordState.removeNode(nodeToRemove);
            for(Integer healthyNode: healthyNodePorts){
                AppConfig.timestampedStandardPrint("Saljem da se ukloni cvor: " + nodeToRemove + " sa cvora: " + healthyNode);
                RemoveNodeMessage rnm = new RemoveNodeMessage(AppConfig.myServentInfo.getListenerPort(), healthyNode, nodeToRemove+"");
                MessageUtil.sendMessage(rnm);
            }

        }
        for (Integer nodeToRemove: toRemoveNodePorts){
            for(Integer healthyNode: healthyNodePorts){
                LoadBackupsMessage lbm = new LoadBackupsMessage(AppConfig.myServentInfo.getListenerPort(), healthyNode, nodeToRemove + "");
                MessageUtil.sendMessage(lbm);
            }
            loadBackupsForNode(nodeToRemove);
        }
    }

    public void lastNodeAction(List<Integer> nodesToRemove){
        for(Integer nodeToRemove: nodesToRemove){
            AppConfig.chordState.removeNode(nodeToRemove);
            loadBackupsForNode(nodeToRemove);
            removeNodeFromBootstrap(nodeToRemove + "");
        }
    }

    private void removeNodeFromBootstrap(String nodeToRemovePort){
        try {
            Socket bsSocket = new Socket("localhost", AppConfig.BOOTSTRAP_PORT);

            PrintWriter bsWriter = new PrintWriter(bsSocket.getOutputStream());
            bsWriter.write("REMOVE\n" + nodeToRemovePort + "\n");
            bsWriter.flush();
            bsSocket.close();
        } catch (IOException e) {
            AppConfig.timestampedErrorPrint("Could not send message to bootstrap server.");
        }
    }

    private void loadBackupsForNode(int nodeToRemovePort){
        int nodeChordId = ChordState.chordHash(nodeToRemovePort);

        List<Backup> backups = AppConfig.backupMap.getBackupForNode(nodeChordId);
        if(backups != null && !backups.isEmpty()){
            for(Backup backup: backups){
                AppConfig.timestampedStandardPrint("Lading backup:" + backup.getChordId());
                MyFile myFile = backup.loadBackup();
                AppConfig.chordState.putValue(myFile.getChordId(), myFile);
            }
        }
        AppConfig.backupMap.removeBackupsForNode(nodeChordId);
    }

    private int getRandomHealthyNodePort(List<Integer> healthyNodes){
        Random random = new Random();
        int randomIndex = random.nextInt(healthyNodes.size());
        return healthyNodes.get(randomIndex);
    }

}
