package servent.message;

public class RemoveFilesFromBackupsMessage extends BasicMessage{
    public RemoveFilesFromBackupsMessage(int senderPort, int receiverPort, String messageText) {
        super(MessageType.REMOVE_FILE_FROM_BACKUPS, senderPort, receiverPort, messageText);
    }
}
