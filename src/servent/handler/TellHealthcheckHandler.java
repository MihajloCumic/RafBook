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
           HeartbeatSharedData sharedData =  HeartbeatSharedData.getInstance();
           int port = clientMessage.getSenderPort();
           if(sharedData.getServentPort() == port){
               AppConfig.timestampedErrorPrint("Node on port: " + port + " is healthy.");
               synchronized (sharedData){
                   sharedData.setIsSuspicious(false);
               }
               return;
           }
            AppConfig.timestampedErrorPrint("Monitoring node on port: " + sharedData.getServentPort() + ", not " + port);
        }else{
            AppConfig.timestampedErrorPrint("Ask healthcheck handler got message that is not TELL_HEALTH_CHECK");
        }
    }
}
