package servent.handler;

import app.AppConfig;
import servent.message.AskHealthcheckMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

public class RecheckNodeHandler implements MessageHandler{
    private final Message clientMessage;

    public RecheckNodeHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.RECHECK_NODE){
            AppConfig.timestampedErrorPrint("RECHECK_NODE");
            int port = Integer.parseInt(clientMessage.getMessageText());
            AskHealthcheckMessage ahm = new AskHealthcheckMessage(clientMessage.getSenderPort(), port);
            MessageUtil.sendMessage(ahm);
        }else{
            AppConfig.timestampedErrorPrint("Recheck node handler got message that is not RECHECK_NODE");
        }
    }
}
