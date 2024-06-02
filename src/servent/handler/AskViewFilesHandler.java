package servent.handler;

import app.AppConfig;
import app.ChordState;
import app.ServentInfo;
import data.file.MyFile;
import data.result.ViewFilesResult;
import data.util.SerializationUtil;
import servent.message.AskViewFilesMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.TellViewFilesMessage;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
                try {
                    String resAsString = SerializationUtil.serialize(makeResultList(filesMap));
                    TellViewFilesMessage tvfm = new TellViewFilesMessage(AppConfig.myServentInfo.getListenerPort(), clientMessage.getSenderPort(), resAsString);
                    MessageUtil.sendMessage(tvfm);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(chordId);
                AskViewFilesMessage avfm = new AskViewFilesMessage(clientMessage.getSenderPort(), nextNode.getListenerPort(), clientMessage.getMessageText());
                MessageUtil.sendMessage(avfm);
            }
        }else{
            AppConfig.timestampedErrorPrint("Ask get handler got a message that is not ASK_VIEW_FILES.");
        }

    }

    private List<ViewFilesResult> makeResultList(Map<Integer, MyFile> map){
        List<ViewFilesResult> resList = new ArrayList<>();
        for(Map.Entry<Integer, MyFile> entry: map.entrySet()){
            MyFile myFile = entry.getValue();
            resList.add(new ViewFilesResult(entry.getKey(), myFile.getName()));
        }
        return resList;
    }
}
