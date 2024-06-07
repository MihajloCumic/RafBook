package servent.handler;

import app.AppConfig;
import app.ChordState;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.RemoveFilesFromBackupsMessage;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class RemoveFileFromBackupsMessage implements MessageHandler{
    private final Message clientMessage;

    public RemoveFileFromBackupsMessage(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.REMOVE_FILE_FROM_BACKUPS){
            if(clientMessage.getSenderPort() == AppConfig.myServentInfo.getListenerPort()) return;
            try{
                int fileKey = Integer.parseInt(clientMessage.getMessageText());
                int nodePort = clientMessage.getSenderPort();
                List<Integer> fileKeys = new ArrayList<>();
                fileKeys.add(fileKey);
                AppConfig.backupMap.removeBackupsForNode(ChordState.chordHash(nodePort), fileKeys);
                RemoveFilesFromBackupsMessage rfbm = new RemoveFilesFromBackupsMessage(clientMessage.getSenderPort(), AppConfig.chordState.getNextNodePort(), clientMessage.getMessageText());
                MessageUtil.sendMessage(rfbm);
            }catch (NumberFormatException e){
                AppConfig.timestampedErrorPrint("Number format exception.");
            }
        }else {
            AppConfig.timestampedErrorPrint("Remove file from backups handler got message that is not REMOVE_FILE_FROM_BACKUPS.");
        }
    }
}
