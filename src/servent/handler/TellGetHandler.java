package servent.handler;

import app.AppConfig;
import data.file.MyFile;
import data.result.GetResult;
import data.util.SerializationUtil;
import servent.message.Message;
import servent.message.MessageType;
import writer.Writer;
import writer.impl.FileWriter;

import java.io.IOException;

public class TellGetHandler implements MessageHandler {

	private Message clientMessage;
	private final Writer writer;
	private final String downloadFolder = "/Downloads";
	
	public TellGetHandler(Message clientMessage) {
		this.writer = new FileWriter();
		this.clientMessage = clientMessage;
	}
	
	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.TELL_GET) {
			String[] parts = clientMessage.getMessageText().split(":", 2);
			
			if (parts.length == 2) {
				try {
					int key = Integer.parseInt(parts[0]);
					GetResult getResult = (GetResult) SerializationUtil.deserialize(parts[1]);
					if (getResult.getResStatus() == -1) {
						AppConfig.timestampedStandardPrint("No such key: " + key);
					} else {
						AppConfig.timestampedStandardPrint(resultAsString(getResult, key));
						MyFile myFile = getResult.getMyFile();
						AppConfig.timestampedStandardPrint(key + ": " + myFile.getName());
						writer.saveFile(AppConfig.root.toString() + downloadFolder, myFile);
						AppConfig.timestampedStandardPrint("Saved file:" + myFile.getName() + " in /Downloads");
					}
				} catch (NumberFormatException e) {
					AppConfig.timestampedErrorPrint("Got TELL_GET message with bad text: " + clientMessage.getMessageText());
				} catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
				AppConfig.timestampedErrorPrint("Got TELL_GET message with bad text: " + clientMessage.getMessageText());
			}
		} else {
			AppConfig.timestampedErrorPrint("Tell get handler got a message that is not TELL_GET");
		}
	}

	private String resultAsString(GetResult getResult, int key){
        return "Key: " + key + " , name: " + getResult.getMyFile().getName();
	}

}
