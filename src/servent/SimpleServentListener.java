package servent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.AppConfig;
import app.Cancellable;
import servent.handler.*;
import servent.message.Message;
import servent.message.util.MessageUtil;

public class SimpleServentListener implements Runnable, Cancellable {

	private volatile boolean working = true;
	
	public SimpleServentListener() {
		
	}

	/*
	 * Thread pool for executing the handlers. Each client will get it's own handler thread.
	 */
	private final ExecutorService threadPool = Executors.newWorkStealingPool();
	
	@Override
	public void run() {
		ServerSocket listenerSocket = null;
		try {
			listenerSocket = new ServerSocket(AppConfig.myServentInfo.getListenerPort(), 100);
			/*
			 * If there is no connection after 1s, wake up and see if we should terminate.
			 */
			listenerSocket.setSoTimeout(1000);
		} catch (IOException e) {
			AppConfig.timestampedErrorPrint("Couldn't open listener socket on: " + AppConfig.myServentInfo.getListenerPort());
			System.exit(0);
		}
		
		
		while (working) {
			try {
				Message clientMessage;
				
				Socket clientSocket = listenerSocket.accept();
				
				//GOT A MESSAGE! <3
				clientMessage = MessageUtil.readMessage(clientSocket);
				
				MessageHandler messageHandler = new NullHandler(clientMessage);
				
				/*
				 * Each message type has it's own handler.
				 * If we can get away with stateless handlers, we will,
				 * because that way is much simpler and less error prone.
				 */
				switch (clientMessage.getMessageType()) {
					case NEW_NODE:
						messageHandler = new NewNodeHandler(clientMessage);
						break;
					case WELCOME:
						messageHandler = new WelcomeHandler(clientMessage);
						break;
					case SORRY:
						messageHandler = new SorryHandler(clientMessage);
						break;
					case UPDATE:
						messageHandler = new UpdateHandler(clientMessage);
						break;
					case PUT:
						messageHandler = new PutHandler(clientMessage);
						break;
					case ASK_GET:
						messageHandler = new AskGetHandler(clientMessage);
						break;
					case TELL_GET:
						messageHandler = new TellGetHandler(clientMessage);
						break;
					case POISON:
						break;
					case ACCESS_DENIED:
						messageHandler = new AccessDeniedHandler(clientMessage);
						break;
					case ASK_VIEW_FILES:
						messageHandler = new AskViewFilesHandler(clientMessage);
						break;
					case TELL_VIEW_FILES:
						messageHandler = new TellViewFilesHandler(clientMessage);
						break;
					case HEARTBEAT_REQUEST:
						messageHandler = new HeartbeatRequestHandler(clientMessage);
						break;
					case HEARTBEAT_RESPONSE:
						messageHandler = new HeartbeatResponseHandler(clientMessage);
						break;
					case RECHECK_NODE:
						messageHandler = new RecheckNodeHandler(clientMessage);
						break;
					case ASK_HEALTH_CHECK:
						messageHandler = new AskHealthcheckHandler(clientMessage);
						break;
					case TELL_HEALTH_CHECK:
						messageHandler = new TellHealthcheckHandler(clientMessage);
						break;
					case BACKUP:
						messageHandler = new BackupHandler(clientMessage);
						break;
					case REMOVE_NODE:
						messageHandler = new RemoveNodeHandler(clientMessage);
						break;
					case LOAD_BACKUPS:
						messageHandler = new LoadBackupsHandler(clientMessage);
						break;
					case REMOVE_BACKUPS:
						messageHandler = new RemoveBackupsHandler(clientMessage);
						break;
					case ASK_DELETE:
						messageHandler = new AskDeleteHandler(clientMessage);
						break;
					case TELL_DELETE:
						messageHandler = new TellDeleteHandler(clientMessage);
						break;
					case REMOVE_FILE_FROM_BACKUPS:
						messageHandler = new RemoveFileFromBackupsMessage(clientMessage);
				}
				
				threadPool.submit(messageHandler);
			} catch (SocketTimeoutException timeoutEx) {
				//Uncomment the next line to see that we are waking up every second.
//				AppConfig.timedStandardPrint("Waiting...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
		this.working = false;
	}

}
