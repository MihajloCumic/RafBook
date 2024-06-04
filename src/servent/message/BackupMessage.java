package servent.message;

public class BackupMessage extends BasicMessage{
    public BackupMessage(int senderPort, int receiverPort, String messageText) {
        super(MessageType.BACKUP, senderPort, receiverPort, messageText);
    }
}
