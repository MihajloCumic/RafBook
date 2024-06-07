package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import data.file.MyFile;
import data.result.DeleteFIleResult;
import data.util.SerializationUtil;
import servent.message.AskDeleteMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.TellDeleteMessage;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.util.Map;

public class AskDeleteHandler implements MessageHandler{
    private final Message clientMessage;

    public AskDeleteHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.ASK_DELETE){
            int key = Integer.parseInt(clientMessage.getMessageText());
            if(AppConfig.chordState.isKeyMine(key)){
                DeleteFIleResult res = new DeleteFIleResult(-1, null, 0);
                Map<Integer, MyFile> valueMap = AppConfig.chordState.getValueMap();
                if(valueMap.containsKey(key)){
                    MyFile myFile = valueMap.get(key);
                    AppConfig.chordState.removeFromValueMap(key);
                    res.setResStatus(1);
                    res.setFileName(myFile.getName());
                    res.setNodePort(AppConfig.myServentInfo.getListenerPort());
                }
                try {
                    String resString = SerializationUtil.serialize(res);
                    TellDeleteMessage tdm = new TellDeleteMessage(AppConfig.myServentInfo.getListenerPort(), clientMessage.getSenderPort(), resString);
                    MessageUtil.sendMessage(tdm);
                } catch (IOException e) {
                    AppConfig.timestampedStandardPrint("Serialization error.");
                }
            }else{
                ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(key);
                AskDeleteMessage adm = new AskDeleteMessage(clientMessage.getSenderPort(), nextNode.getListenerPort(), clientMessage.getMessageText());
                MessageUtil.sendMessage(adm);
            }
        }else{
            AppConfig.timestampedErrorPrint("Ask delete handler got message that is not ASK_DELETE");
        }
    }
}
