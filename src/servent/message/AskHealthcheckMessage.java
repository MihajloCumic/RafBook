package servent.message;

public class AskHealthcheckMessage extends BasicMessage{
    public AskHealthcheckMessage(int senderPort, int receiverPort) {
        super(MessageType.ASK_HEALTH_CHECK, senderPort, receiverPort);
    }
}
