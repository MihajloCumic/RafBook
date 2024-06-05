package servent.handler;

import app.AppConfig;
import servent.message.LoadBackupsMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.RemoveNodeMessage;
import servent.message.util.MessageUtil;

public class RemoveNodeHandler implements MessageHandler{
    private final Message clientMessage;

    public RemoveNodeHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        //dht_get 2
        if(clientMessage.getMessageType() == MessageType.REMOVE_NODE){
            if(AppConfig.myServentInfo.getListenerPort() == clientMessage.getSenderPort()){
                LoadBackupsMessage lbm = new LoadBackupsMessage(clientMessage.getSenderPort(), AppConfig.chordState.getNextNodePort(), clientMessage.getMessageText());
                MessageUtil.sendMessage(lbm);
                return;
            }
            try {
                int nodeToRemovePort = Integer.parseInt(clientMessage.getMessageText());
                AppConfig.chordState.removeNode(nodeToRemovePort);
            }catch (NumberFormatException e){
                AppConfig.timestampedErrorPrint(e.getLocalizedMessage());
            }
            RemoveNodeMessage rnm = new RemoveNodeMessage(clientMessage.getSenderPort(), AppConfig.chordState.getNextNodePort(), clientMessage.getMessageText());
            MessageUtil.sendMessage(rnm);

        }else{
            AppConfig.timestampedErrorPrint("Remove node handler got message that is not REMOVE_NODE");
        }

    }
}
