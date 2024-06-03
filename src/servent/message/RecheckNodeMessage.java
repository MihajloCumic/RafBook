package servent.message;

public class RecheckNodeMessage extends BasicMessage{
    public RecheckNodeMessage(int senderPort, int receiverPort, String messageText) {
        super(MessageType.RECHECK_NODE, senderPort, receiverPort, messageText);
    }
}
