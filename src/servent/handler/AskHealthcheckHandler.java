package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.TellHealthcheckMessage;
import servent.message.util.MessageUtil;

public class AskHealthcheckHandler implements MessageHandler{
    private final Message clientMessage;

    public AskHealthcheckHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.ASK_HEALTH_CHECK){
            AppConfig.timestampedErrorPrint("ASK_HEALTH_CHECK");
            TellHealthcheckMessage thm = new TellHealthcheckMessage(clientMessage.getReceiverPort(), clientMessage.getSenderPort());
            MessageUtil.sendMessage(thm);
        }else{
            AppConfig.timestampedErrorPrint("Ask healthcheck handler got message that is not ASK_HEALTH_CHECK");
        }

    }
}
