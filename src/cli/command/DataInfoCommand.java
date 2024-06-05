package cli.command;

import app.AppConfig;
import app.ServentInfo;
import data.backup.Backup;
import data.file.MyFile;

import java.util.List;
import java.util.Map;

public class DataInfoCommand implements CLICommand{
    @Override
    public String commandName() {
        return "data-info";
    }

    @Override
    public void execute(String args) {
        //dht_get 2
        System.out.println("NodeId-> " + AppConfig.myServentInfo.getChordId());
        System.out.println("---------------");
        Map<Integer, MyFile> map = AppConfig.chordState.getValueMap();
        for(Map.Entry<Integer, MyFile> entry: map.entrySet()){
            System.out.println("Key-> " + entry.getKey() + " :::: Value" + entry.getValue().toString());
        }
        System.out.println("All nodes: ");
        List<ServentInfo> allNodes = AppConfig.chordState.getAllNodeInfo();
        for(ServentInfo node: allNodes){
            System.out.println(node.getChordId());
        }
        System.out.println("Successors:");
        ServentInfo[] successors = AppConfig.chordState.getSuccessorTable();
        if(successors == null) return;
        for(int i = 0; i < successors.length; i++){
            System.out.println(successors[i].getChordId());
        }
        System.out.println("My file backups locations: ");
        for(Map.Entry<Integer, Integer> entry: AppConfig.backupMap.getMyBackupLocations().entrySet()){
            System.out.println("file: " + entry.getKey() + ", on node: " + entry.getValue());
        }
        System.out.println("Backups on this node: ");
        for(Map.Entry<Integer, List<Backup>> entry: AppConfig.backupMap.getBackups().entrySet()){
            System.out.println("Node  " + entry.getKey() + ":");
            for(Backup backup: entry.getValue()){
                System.out.println("\tfile: " + backup.getChordId());
            }
        }
    }
}
