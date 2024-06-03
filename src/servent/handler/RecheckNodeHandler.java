package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;

public class RecheckNodeHandler implements MessageHandler{
    private final Message clientMessage;

    public RecheckNodeHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.RECHECK_NODE){
            AppConfig.timestampedErrorPrint("NOde: " + clientMessage.getMessageText() + " must rechek this node");
        }else{
            AppConfig.timestampedErrorPrint("Recheck node handler got message that is not RECHECK_NODE");
        }
    }
}
