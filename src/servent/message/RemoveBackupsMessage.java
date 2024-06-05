package servent.message;

public class RemoveBackupsMessage extends BasicMessage{
    public RemoveBackupsMessage(int senderPort, int receiverPort, String messageText) {
        super(MessageType.REMOVE_BACKUPS, senderPort, receiverPort, messageText);
    }
}
