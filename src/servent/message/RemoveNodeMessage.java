package servent.message;

public class RemoveNodeMessage extends BasicMessage{
    public RemoveNodeMessage(int senderPort, int receiverPort, String messageText) {
        super(MessageType.REMOVE_NODE, senderPort, receiverPort, messageText);
    }
}
