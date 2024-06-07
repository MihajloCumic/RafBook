package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;

public class TellDeleteHandler implements MessageHandler{
    private final Message clientMessage;

    public TellDeleteHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.TELL_DELETE){

        }else{
            AppConfig.timestampedErrorPrint("Tell delete handler got message that is not TELL_DELETE");
        }
    }
}
