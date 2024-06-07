package cli.command;

import app.AppConfig;
import app.ChordState;
import data.file.MyFile;
import data.result.GetResult;
import writer.Writer;
import writer.impl.FileWriter;

public class DHTGetCommand implements CLICommand {
	private final Writer writer;
	private final String downloadFolder = "/Downloads";

	public DHTGetCommand() {
		this.writer = new FileWriter();
	}

	@Override
	public String commandName() {
		return "dht_get";
	}

	@Override
	public void execute(String args) {
		try {
			//int key = Math.abs(args.hashCode());
			//int chordKey = ChordState.chordHash(key);
			int key = Integer.parseInt(args);
			GetResult result = AppConfig.chordState.getValue(key);
			
			if (result.getResStatus() == -2) {
				AppConfig.timestampedStandardPrint("Please wait...");
			} else if (result.getResStatus() == -1) {
				AppConfig.timestampedStandardPrint("No such key: " + key);
			} else {
				MyFile myFile = result.getMyFile();
				AppConfig.timestampedStandardPrint(myFile.getChordId() + ": " + myFile.getName());
				writer.saveFile(AppConfig.root.toString() + downloadFolder, myFile);
				AppConfig.timestampedStandardPrint("Saved file:" + myFile.getName() + " in /Downloads");

			}
		} catch (NumberFormatException e) {
			AppConfig.timestampedErrorPrint("Invalid argument for dht_get: " + args + ". Should be key, which is an int.");
		}
	}



}
