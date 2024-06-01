package servent.message;

public class AskViewFilesMessage extends BasicMessage{
    public AskViewFilesMessage( int senderPort, int receiverPort, String messageText) {
        super(MessageType.ASK_VIEW_FILES, senderPort, receiverPort, messageText);
    }
}
