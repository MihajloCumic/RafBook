package servent.handler;

import app.AppConfig;
import app.ChordState;
import data.backup.Backup;
import data.file.MyFile;
import data.util.SerializationUtil;
import servent.message.BackupMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.io.IOException;

public class BackupHandler implements MessageHandler{
    private final Message clientMessage;

    public BackupHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.BACKUP){
            try {
                String[] splitMessage = clientMessage.getMessageText().split(":", 2);
                if(splitMessage.length != 2){
                    AppConfig.timestampedErrorPrint("BackupHandler bad message error.");
                    return;
                }
                int replicaCnt = Integer.parseInt(splitMessage[0]);
                MyFile myFile =(MyFile) SerializationUtil.deserialize(splitMessage[1]);
                AppConfig.backupMap.backupFile(myFile, ChordState.chordHash(clientMessage.getSenderPort()));
                AppConfig.timestampedStandardPrint("Backed up file: " + myFile.getName() + " from node: " + clientMessage.getSenderPort());
                if(clientMessage.getSenderPort() != AppConfig.chordState.getNextNodePort() && replicaCnt < 3){
                    int secondBackupNodePort = AppConfig.chordState.getNextNodePort();
                    BackupMessage bm = new BackupMessage(clientMessage.getSenderPort(), secondBackupNodePort, ++replicaCnt + ":" + splitMessage[1]);
                    MessageUtil.sendMessage(bm);
                }
            } catch (IOException | ClassNotFoundException e) {
                AppConfig.timestampedErrorPrint("Could not deserialize: " + clientMessage.getMessageText());
            }
        }else{
            AppConfig.timestampedErrorPrint("Backup handler got message that is not BACKUP");
        }
    }
}
