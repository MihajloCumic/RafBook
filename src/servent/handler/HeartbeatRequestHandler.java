package servent.handler;

import app.AppConfig;
import servent.message.HeartbeatResponseMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

public class HeartbeatRequestHandler implements MessageHandler{
    private final Message clientMessage;

    public HeartbeatRequestHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.HEARTBEAT_REQUEST){
            HeartbeatResponseMessage hr = new HeartbeatResponseMessage(clientMessage.getReceiverPort(), clientMessage.getSenderPort());
            MessageUtil.sendMessage(hr);
        }else{
            AppConfig.timestampedErrorPrint("Ask get handler got a message that is not HEARTBEAT_REQUEST");
        }
    }
}
