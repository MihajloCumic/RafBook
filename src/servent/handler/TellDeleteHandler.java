package servent.handler;

import app.AppConfig;
import data.result.DeleteFIleResult;
import data.util.SerializationUtil;
import servent.message.Message;
import servent.message.MessageType;

import java.io.IOException;

public class TellDeleteHandler implements MessageHandler{
    private final Message clientMessage;

    public TellDeleteHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.TELL_DELETE){
            try {
                DeleteFIleResult res = (DeleteFIleResult) SerializationUtil.deserialize(clientMessage.getMessageText());
                if(res.getResStatus() == -1){
                    AppConfig.timestampedErrorPrint("No node with this id.");
                    return;
                }
                if(res.getResStatus() == 1){
                    AppConfig.timestampedStandardPrint("Deleted file with name: " + res.getFileName() + " from node: " + res.getNodePort());
                    return;
                }
                AppConfig.timestampedErrorPrint("Error in DeleteCommand.");
            } catch (IOException | ClassNotFoundException e) {
                AppConfig.timestampedErrorPrint("Deserialization error.");
            }
        }else{
            AppConfig.timestampedErrorPrint("Tell delete handler got message that is not TELL_DELETE");
        }
    }
}
