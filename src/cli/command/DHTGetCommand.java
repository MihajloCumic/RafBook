package cli.command;

import app.AppConfig;
import app.ChordState;
import data.result.GetResult;

public class DHTGetCommand implements CLICommand {

	@Override
	public String commandName() {
		return "dht_get";
	}

	@Override
	public void execute(String args) {
		try {
			int key = Integer.parseInt(args);
			int chordKey = ChordState.chordHash(key);
			GetResult result = AppConfig.chordState.getValue(chordKey);
			
			if (result.getResStatus() == -2) {
				AppConfig.timestampedStandardPrint("Please wait...");
			} else if (result.getResStatus() == -1) {
				AppConfig.timestampedStandardPrint("No such key: " + key);
			} else {
				AppConfig.timestampedStandardPrint(key + ": " + result.getMyFile());
			}
		} catch (NumberFormatException e) {
			AppConfig.timestampedErrorPrint("Invalid argument for dht_get: " + args + ". Should be key, which is an int.");
		}
	}

}
