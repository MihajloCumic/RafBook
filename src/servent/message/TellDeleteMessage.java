package servent.message;

public class TellDeleteMessage extends BasicMessage{
    public TellDeleteMessage(int senderPort, int receiverPort, String messageText) {
        super(MessageType.TELL_DELETE, senderPort, receiverPort, messageText);
    }
}
