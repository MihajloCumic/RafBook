package servent.handler;

import app.AppConfig;
import app.ChordState;
import data.backup.Backup;
import data.file.MyFile;
import servent.message.LoadBackupsMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.util.List;

public class LoadBackupsHandler implements MessageHandler{
    private final Message clientMessage;

    public LoadBackupsHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.LOAD_BACKUPS){
            try {
                int nodeToRemovePort = Integer.parseInt(clientMessage.getMessageText());
                int nodeChordId = ChordState.chordHash(nodeToRemovePort);

                List<Backup> backups = AppConfig.backupMap.getBackupForNode(nodeChordId);
                if(backups != null && !backups.isEmpty()){
                    for(Backup backup: backups){
                        MyFile myFile = backup.loadBackup();
                        AppConfig.chordState.putValue(myFile.getChordId(), myFile);
                    }
                }
                AppConfig.backupMap.removeBackupsForNode(nodeChordId);
                if(AppConfig.myServentInfo.getListenerPort() == clientMessage.getSenderPort()) return;
                LoadBackupsMessage lbm = new LoadBackupsMessage(clientMessage.getSenderPort(), AppConfig.chordState.getNextNodePort(), clientMessage.getMessageText());
                MessageUtil.sendMessage(lbm);
            }catch (NumberFormatException e){
                AppConfig.timestampedErrorPrint(e.getLocalizedMessage());
            }
        }else{
            AppConfig.timestampedErrorPrint("Load backups handler got message that is not LOAD_BACKUPS");
        }
    }
}
