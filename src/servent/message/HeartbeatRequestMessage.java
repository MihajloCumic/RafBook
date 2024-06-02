package servent.message;

public class HeartbeatRequestMessage extends BasicMessage{
    public HeartbeatRequestMessage( int senderPort, int receiverPort) {
        super(MessageType.HEARTBEAT_REQUEST, senderPort, receiverPort);
    }
}
