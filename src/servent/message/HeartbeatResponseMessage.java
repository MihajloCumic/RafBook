package servent.message;

public class HeartbeatResponseMessage extends BasicMessage{
    public HeartbeatResponseMessage( int senderPort, int receiverPort) {
        super(MessageType.HEARTBEAT_RESPONSE, senderPort, receiverPort);
    }
}
