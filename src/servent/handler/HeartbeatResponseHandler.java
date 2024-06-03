package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;

public class HeartbeatResponseHandler implements MessageHandler{
    private final Message clientMessage;

    public HeartbeatResponseHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.HEARTBEAT_RESPONSE){
            AppConfig.timestampedErrorPrint("Got HEARTBEAT_RESPONSE");
        }else{
            AppConfig.timestampedErrorPrint("Ask get handler got a message that is not HEARTBEAT_Response");
        }
    }
}
