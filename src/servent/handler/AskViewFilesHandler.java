package servent.handler;

import servent.message.Message;

public class AskViewFilesHandler implements MessageHandler{
    private final Message clientMessage;

    public AskViewFilesHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {

    }
}
