package servent.handler;

import java.io.IOException;
import java.util.Map;

import app.AppConfig;
import app.ServentInfo;
import data.file.MyFile;
import data.result.GetResult;
import data.util.SerializationUtil;
import servent.message.*;
import servent.message.util.MessageUtil;

public class AskGetHandler implements MessageHandler {

	private Message clientMessage;
	
	public AskGetHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}
	
	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.ASK_GET) {
			try {
				int key = Integer.parseInt(clientMessage.getMessageText());
				if (AppConfig.chordState.isKeyMine(key)) {
					Map<Integer, MyFile> valueMap = AppConfig.chordState.getValueMap();
					GetResult getResult = new GetResult(-1, null);
					
					if (valueMap.containsKey(key)) {
						getResult.setMyFile(valueMap.get(key));
						getResult.setResStatus(1);
					}
					String getResultStr = SerializationUtil.serialize(getResult);
					TellGetMessage tgm = new TellGetMessage(AppConfig.myServentInfo.getListenerPort(), clientMessage.getSenderPort(),
															key, getResultStr);
					MessageUtil.sendMessage(tgm);
				} else {
					ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(key);
					AskGetMessage agm = new AskGetMessage(clientMessage.getSenderPort(), nextNode.getListenerPort(), clientMessage.getMessageText());
					MessageUtil.sendMessage(agm);
				}
			} catch (NumberFormatException e) {
				AppConfig.timestampedErrorPrint("Got ask get with bad text: " + clientMessage.getMessageText());
			} catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
			AppConfig.timestampedErrorPrint("Ask get handler got a message that is not ASK_GET");
		}

	}

}