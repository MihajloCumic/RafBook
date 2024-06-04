package servent.message;

public class TellHealthcheckMessage extends BasicMessage{
    public TellHealthcheckMessage(int senderPort, int receiverPort) {
        super(MessageType.TELL_HEALTH_CHECK, senderPort, receiverPort);
    }
}
