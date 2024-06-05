package servent.message;

public class LoadBackupsMessage extends BasicMessage{
    public LoadBackupsMessage(int senderPort, int receiverPort, String messageText) {
        super(MessageType.LOAD_BACKUPS, senderPort, receiverPort, messageText);
    }
}
