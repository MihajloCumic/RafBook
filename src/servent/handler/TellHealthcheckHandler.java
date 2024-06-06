package servent.handler;

import app.AppConfig;
import heartbeat.HeartbeatSharedData;
import servent.message.Message;
import servent.message.MessageType;

public class TellHealthcheckHandler implements MessageHandler{
    private final Message clientMessage;

    public TellHealthcheckHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.TELL_HEALTH_CHECK){
            AppConfig.timestampedErrorPrint("TELL_HEALTH_CHECK");
            HeartbeatSharedData sharedData = HeartbeatSharedData.getInstance();
            sharedData.hasResponded(clientMessage.getSenderPort());
        }else{
            AppConfig.timestampedErrorPrint("Ask healthcheck handler got message that is not TELL_HEALTH_CHECK");
        }
    }
}
