package data.backup;

import app.AppConfig;
import app.ChordState;
import data.file.MyFile;
import data.result.GetResult;
import data.util.SerializationUtil;
import servent.message.BackupMessage;
import servent.message.util.MessageUtil;
import writer.Writer;
import writer.impl.FileWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackupMap {
    private final Path root;
    private final Map<Integer, Integer> myBackupLocations;
    private final Map<Integer, List<Backup>> backups;
    private final Writer writer;

    public BackupMap(Path root) {
        this.root = root;
        this.myBackupLocations = new HashMap<>();
        this.backups = new HashMap<>();
        this.writer = new FileWriter();
    }

    public void backupFile(MyFile file, int nodeChordId){
        String saveFileName = "/"+ file.getChordId();
        String extension = file.getExtensionAsString();
        writer.saveFile(root.toString(), saveFileName, file);

        String location = root.toString() + saveFileName + extension;
        Backup backup = new Backup(location, file.getName(), file.isPrivate(), file.getFileType(), file.getChordId());
        List<Backup> backupList;
        if(backups.containsKey(nodeChordId)){
            backupList = backups.get(nodeChordId);
        }else{
            backupList = new ArrayList<>();
        }
        backupList.add(backup);
        backups.put(nodeChordId,backupList);
    }

    public List<Backup> getBackupForNode(int nodeChordId){
        if(backups.containsKey(nodeChordId)){
            return backups.get(nodeChordId);
        }
        return null;
    }

    public void removeBackupsForNode(int nodeChordId){
        backups.remove(nodeChordId);
    }

    public void removeBackupsForNode(int nodeChordId, List<Integer> fileChordIds){
        if(backups.containsKey(nodeChordId)){
            List<Backup> backupList = backups.get(nodeChordId);
            List<Backup> newBackups = new ArrayList<>();
            for(Backup backup: backupList){
                if(fileChordIds.contains(backup.getChordId())) continue;
                newBackups.add(backup);
            }
            backups.put(nodeChordId, newBackups);
        }
        for(Integer chordId: fileChordIds){
            myBackupLocations.remove(chordId);
        }
    }

    public void addToMyBackupLocations(int fileChordId,int nodeChordId){
        myBackupLocations.put(fileChordId, nodeChordId);
    }

    public void removeFromMyBackupLocations(int nodeChordId){
        for(Map.Entry<Integer, Integer> entry: myBackupLocations.entrySet()){
            if(entry.getValue() == nodeChordId){
                GetResult getResult = AppConfig.chordState.getMyFileBYChordId(entry.getKey());
                if(getResult.getResStatus() != 1){
                    AppConfig.timestampedErrorPrint("File with id: " + entry.getKey() + " does not exist.");
                    return;
                }
                MyFile myFile = getResult.getMyFile();
                try {
                    String fileAsString = SerializationUtil.serialize(myFile);
                    int backupNodePort = AppConfig.chordState.getRandomHealthyNodePort();
                    int backupNodeChordId = ChordState.chordHash(backupNodePort);

                    BackupMessage backupMessage = new BackupMessage(AppConfig.myServentInfo.getListenerPort(), backupNodePort, fileAsString);
                    MessageUtil.sendMessage(backupMessage);
                    entry.setValue(backupNodeChordId);
                } catch (IOException e) {
                    AppConfig.timestampedErrorPrint("Could not serialize file with id: " + entry.getKey());
                }
            }
        }
    }

    public Map<Integer, List<Backup>> getBackups() {
        return backups;
    }

    public Map<Integer, Integer> getMyBackupLocations() {
        return myBackupLocations;
    }
}
