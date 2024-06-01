package servent.message;

public class AccessDeniedMessage extends BasicMessage{
    private static final long serialVersionUID = -6222957923983818966L;
    public AccessDeniedMessage(int senderPort, int receiverPort, String messageText) {
        super(MessageType.ACCESS_DENIED, senderPort, receiverPort, messageText);
    }
}
