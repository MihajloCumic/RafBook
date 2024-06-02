package servent.handler;

import app.AppConfig;
import data.result.ViewFilesResult;
import data.util.SerializationUtil;
import servent.message.Message;
import servent.message.MessageType;

import java.io.IOException;
import java.util.List;

public class TellViewFilesHandler implements MessageHandler{
    private final Message clientMessage;

    public TellViewFilesHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.TELL_VIEW_FILES){
            try {
                @SuppressWarnings("unchecked")
                List<ViewFilesResult> resultList = (List<ViewFilesResult>) SerializationUtil.deserialize(clientMessage.getMessageText());
                StringBuilder sb = new StringBuilder();
                sb.append("Node localhost:").append(clientMessage.getSenderPort()).append(" contains:");
                for(ViewFilesResult viewFilesResult: resultList){
                    sb.append("\n\t file: chordId- ").append(viewFilesResult.getChordId()).append(" , name- ").append(viewFilesResult.getName());
                }
                AppConfig.timestampedStandardPrint(sb.toString());

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            AppConfig.timestampedErrorPrint("Ask get handler got a message that is not TELL_VIEW_FILES.");
        }
    }
}
