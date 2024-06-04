package servent.handler;

import app.AppConfig;
import app.ChordState;
import data.file.MyFile;
import data.util.SerializationUtil;
import servent.message.Message;
import servent.message.MessageType;

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
                MyFile myFile =(MyFile) SerializationUtil.deserialize(clientMessage.getMessageText());
                AppConfig.backupMap.backupFile(myFile, ChordState.chordHash(clientMessage.getSenderPort()));
                AppConfig.timestampedStandardPrint("Backed up file: " + myFile.getName() + " from node: " + clientMessage.getSenderPort());
            } catch (IOException | ClassNotFoundException e) {
                AppConfig.timestampedErrorPrint("Could not deserialize: " + clientMessage.getMessageText());
            }
        }else{
            AppConfig.timestampedErrorPrint("Backup handler got message that is not BACKUP");
        }
    }
}
