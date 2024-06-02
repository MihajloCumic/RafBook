package servent.message;

public class TellViewFilesMessage extends BasicMessage{
    public TellViewFilesMessage(int senderPort, int receiverPort, String messageText) {
        super(MessageType.TELL_VIEW_FILES, senderPort, receiverPort, messageText);
    }
}
