package servent.handler;

import app.AppConfig;
import data.file.MyFile;
import servent.message.Message;
import servent.message.MessageType;

public class PutHandler implements MessageHandler {

	private Message clientMessage;
	
	public PutHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}
	
	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.PUT) {
			String[] splitText = clientMessage.getMessageText().split(":", 2);
			if (splitText.length == 2) {
				int key = 0;
				
				try {
					key = Integer.parseInt(splitText[0]);
					String value = splitText[1];
					
					AppConfig.chordState.putValue(key, new MyFile(value));
				} catch (NumberFormatException e) {
					AppConfig.timestampedErrorPrint("Got put message with bad text: " + clientMessage.getMessageText());
				}
			} else {
				AppConfig.timestampedErrorPrint("Got put message with bad text: " + clientMessage.getMessageText());
			}
			
			
		} else {
			AppConfig.timestampedErrorPrint("Put handler got a message that is not PUT");
		}

	}

}
