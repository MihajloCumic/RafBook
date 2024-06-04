package data.backup;

import data.file.MyFile;
import writer.Writer;
import writer.impl.FileWriter;

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

    public Map<Integer, List<Backup>> getBackups() {
        return backups;
    }

    public Map<Integer, Integer> getMyBackupLocations() {
        return myBackupLocations;
    }
}
