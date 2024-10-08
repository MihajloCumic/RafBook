package app;

import cli.CLIParser;
import heartbeat.Heartbeat;
import servent.SimpleServentListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Describes the procedure for starting a single Servent
 *
 * @author bmilojkovic
 */
public class ServentMain {

	/**
	 * Command line arguments are:
	 * 0 - path to servent list file
	 * 1 - this servent's id
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			AppConfig.timestampedErrorPrint("Please provide servent list file and id of this servent.");
		}
		
		int serventId = -1;
		int portNumber = -1;
		
		String serventListFile = args[0];
		
		try {
			serventId = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			AppConfig.timestampedErrorPrint("Second argument should be an int. Exiting...");
			System.exit(0);
		}
		
		AppConfig.readConfig(serventListFile, serventId);

		boolean createdFolder = createServentFolder();
		if(!createdFolder){
			AppConfig.timestampedErrorPrint("Could not create folder for this servent.");
			System.exit(-1);
		}
		
		try {
			portNumber = AppConfig.myServentInfo.getListenerPort();
			
			if (portNumber < 1000 || portNumber > 2000) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			AppConfig.timestampedErrorPrint("Port number should be in range 1000-2000. Exiting...");
			System.exit(0);
		}
		
		AppConfig.timestampedStandardPrint("Starting servent " + AppConfig.myServentInfo);

		SimpleServentListener simpleListener = new SimpleServentListener();
		Thread listenerThread = new Thread(simpleListener);
		listenerThread.start();

		Heartbeat heartbeat = new Heartbeat(AppConfig.lowWaitingTime, AppConfig.highWaitingTime);

		CLIParser cliParser = new CLIParser(simpleListener, heartbeat);
		Thread cliThread = new Thread(cliParser);
		cliThread.start();

		ServentInitializer serventInitializer = new ServentInitializer();
		Thread initializerThread = new Thread(serventInitializer);
		initializerThread.start();

		Thread heartbeatThread = new Thread(heartbeat);
		heartbeatThread.start();
		
	}

	private static boolean createServentFolder(){
		Path path = AppConfig.root;
        try {
            Files.createDirectories(path);
			Files.createDirectories(Paths.get(path.toString() +"/Downloads"));
			return true;
        } catch (IOException e) {
            return false;
        }
    }
}
