package servent.handler;

import app.AppConfig;
import heartbeat.Heartbeat;
import heartbeat.HeartbeatSharedData;
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
            HeartbeatSharedData.getInstance().hasResponded(clientMessage.getSenderPort());
        }else{
            AppConfig.timestampedErrorPrint("Ask get handler got a message that is not HEARTBEAT_Response");
        }
    }
}
