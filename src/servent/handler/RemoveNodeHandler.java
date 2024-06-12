package servent.handler;

import app.AppConfig;
import servent.message.LoadBackupsMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.RemoveNodeMessage;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class RemoveNodeHandler implements MessageHandler{
    private final Message clientMessage;

    public RemoveNodeHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        //data-info
        if(clientMessage.getMessageType() == MessageType.REMOVE_NODE){
            if(AppConfig.myServentInfo.getListenerPort() == clientMessage.getSenderPort()){
                removeNodeFromBootstrap(clientMessage.getMessageText());
//                LoadBackupsMessage lbm = new LoadBackupsMessage(clientMessage.getSenderPort(), AppConfig.chordState.getNextNodePort(), clientMessage.getMessageText());
//                MessageUtil.sendMessage(lbm);
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

    private void removeNodeFromBootstrap(String nodeToRemovePort){
        try {
            Socket bsSocket = new Socket("localhost", AppConfig.BOOTSTRAP_PORT);

            PrintWriter bsWriter = new PrintWriter(bsSocket.getOutputStream());
            bsWriter.write("REMOVE\n" + nodeToRemovePort + "\n");
            bsWriter.flush();
            bsSocket.close();
        } catch (IOException e) {
            AppConfig.timestampedErrorPrint("Could not send message to bootstrap server.");
        }
    }

}
