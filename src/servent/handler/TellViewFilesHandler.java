package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;

public class TellViewFilesHandler implements MessageHandler{
    private final Message clientMessage;

    public TellViewFilesHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.TELL_VIEW_FILES){
            AppConfig.timestampedStandardPrint("On node localhost:" + clientMessage.getSenderPort()+ ":\n" +  clientMessage.getMessageText());
        }else {
            AppConfig.timestampedErrorPrint("Ask get handler got a message that is not TELL_VIEW_FILES.");
        }
    }
}
