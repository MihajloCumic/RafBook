package servent.handler;

import app.AppConfig;
import app.ChordState;
import app.ServentInfo;
import data.file.MyFile;
import servent.message.AskViewFilesMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.TellViewFilesMessage;
import servent.message.util.MessageUtil;

import java.util.Map;

public class AskViewFilesHandler implements MessageHandler{
    private final Message clientMessage;

    public AskViewFilesHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.ASK_VIEW_FILES){
            int chordId = ChordState.chordHash(Integer.parseInt(clientMessage.getMessageText()));
            if(AppConfig.myServentInfo.getChordId() == chordId){
                Map<Integer, MyFile> filesMap = AppConfig.chordState.getValueMap();
                String keys = filesMap.keySet().toString();
                TellViewFilesMessage tvfm = new TellViewFilesMessage(AppConfig.myServentInfo.getListenerPort(), clientMessage.getSenderPort(), keys);
                MessageUtil.sendMessage(tvfm);
            }else{
                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(chordId);
                AskViewFilesMessage avfm = new AskViewFilesMessage(clientMessage.getSenderPort(), nextNode.getListenerPort(), clientMessage.getMessageText());
                MessageUtil.sendMessage(avfm);
            }
        }else{
            AppConfig.timestampedErrorPrint("Ask get handler got a message that is not ASK_VIEW_FILES.");
        }

    }
}
