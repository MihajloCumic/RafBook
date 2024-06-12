package servent.handler;

import app.AppConfig;
import app.ChordState;
import data.file.MyFile;
import data.util.SerializationUtil;
import heartbeat.Heartbeat;
import servent.message.*;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WelcomeHandler implements MessageHandler {

	private Message clientMessage;
	
	public WelcomeHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}
	
	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.WELCOME) {
			WelcomeMessage welcomeMsg = (WelcomeMessage)clientMessage;
			
			AppConfig.chordState.init(welcomeMsg);
			
			UpdateMessage um = new UpdateMessage(AppConfig.myServentInfo.getListenerPort(), AppConfig.chordState.getNextNodePort(), "");
			MessageUtil.sendMessage(um);
			//saljemo sve vrednosti cvorovima da ih uklone ako ih imaju sacuvane kao backup-ove
			List<Integer> fileChordIds = new ArrayList<>();
			for(Map.Entry<Integer, MyFile> entry: welcomeMsg.getValues().entrySet()){
				fileChordIds.add(entry.getKey());
			}
            try {
                String msg = ChordState.chordHash(clientMessage.getSenderPort()) + ":" + SerializationUtil.serialize(fileChordIds);
				RemoveBackupsMessage rbm = new RemoveBackupsMessage(AppConfig.myServentInfo.getListenerPort(), AppConfig.chordState.getNextNodePort(), msg);
				MessageUtil.sendMessage(rbm);
			} catch (IOException e) {
                throw new RuntimeException(e);
            }
		} else {
			AppConfig.timestampedErrorPrint("Welcome handler got a message that is not WELCOME");
		}

	}

}
