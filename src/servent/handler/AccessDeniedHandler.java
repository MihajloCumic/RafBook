package servent.handler;

import app.AppConfig;
import servent.message.AccessDeniedMessage;
import servent.message.Message;
import servent.message.MessageType;

public class AccessDeniedHandler implements MessageHandler{
    private final Message clientMessage;
    public AccessDeniedHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }
    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.ACCESS_DENIED){
            AppConfig.timestampedStandardPrint(clientMessage.getMessageText());
        }else AppConfig.timestampedErrorPrint("Ask get handler got a message that is not ACCESS_DENIED");
    }
}
