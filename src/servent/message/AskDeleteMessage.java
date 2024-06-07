package servent.message;

public class AskDeleteMessage extends BasicMessage{
    public AskDeleteMessage(int senderPort, int receiverPort, String messageText) {
        super(MessageType.ASK_DELETE, senderPort, receiverPort, messageText);
    }
}
