package servent.handler;

import app.AppConfig;
import app.ChordState;
import data.util.SerializationUtil;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.RemoveBackupsMessage;
import servent.message.util.MessageUtil;

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
            if(AppConfig.myServentInfo.getListenerPort() == clientMessage.getSenderPort()){
                AppConfig.timestampedErrorPrint("FInished remove backups cycle.");
                return;
            }
            try {
                String[] splitMsg = clientMessage.getMessageText().split(":", 2);
                if(splitMsg.length != 2){
                    AppConfig.timestampedErrorPrint("Remove handler error: bad message.");
                    return;
                }
                List<Integer> filesToRemove = (List<Integer>) SerializationUtil.deserialize(splitMsg[1]);
                int nodeChordId = Integer.parseInt(splitMsg[0]);
                AppConfig.timestampedErrorPrint("Removing backups of node: " + nodeChordId);
                AppConfig.backupMap.removeBackupsForNode(nodeChordId, filesToRemove);
                RemoveBackupsMessage rbm = new RemoveBackupsMessage(clientMessage.getSenderPort(), AppConfig.chordState.getNextNodePort(), clientMessage.getMessageText());

//                RemoveBackupsMessage rbm = new RemoveBackupsMessage(clientMessage.getSenderPort(), AppConfig.chordState.getNextNodePort(), clientMessage.getMessageText());
                MessageUtil.sendMessage(rbm);

            } catch (IOException | ClassNotFoundException e) {
                AppConfig.timestampedErrorPrint("Deserializatin error: RemoveBackupHandler.");
            }
        }else{
            AppConfig.timestampedErrorPrint("Remove backups handler hot message that is not REMOVE_BACKUPS");
        }

    }
}
