package servent.handler;

import app.AppConfig;
import app.ChordState;
import data.util.SerializationUtil;
import servent.message.Message;
import servent.message.MessageType;

import java.io.IOException;
import java.util.List;

public class RemoveBackupsHandler implements MessageHandler{
    private final Message clientMessage;

    public RemoveBackupsHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.REMOVE_BACKUPS){
            try {
                List<Integer> filesToRemove = (List<Integer>) SerializationUtil.deserialize(clientMessage.getMessageText());
                int nodeChordId = ChordState.chordHash(clientMessage.getSenderPort());
                AppConfig.backupMap.removeBackupsForNode(nodeChordId, filesToRemove);

            } catch (IOException | ClassNotFoundException e) {
                AppConfig.timestampedErrorPrint("Deserializatin error: RemoveBackupHandler.");
            }
        }else{
            AppConfig.timestampedErrorPrint("Remove backups handler hot message that is not REMOVE_BACKUPS");
        }

    }
}
