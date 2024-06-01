package cli.command;

import app.AppConfig;
import app.ChordState;
import data.file.MyFile;
import reader.Reader;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DHTPutCommand implements CLICommand {
	private final String absolutePath = "/home/cuma/Fakultet/letnji-semestar/konkurenti-distribuirani/domaci/projekat/KiDS-vezbe9/resources/";
	private final Reader fileReader;

	public DHTPutCommand(Reader fileReader) {
		this.fileReader = fileReader;
	}

	@Override
	public String commandName() {
		return "dht_put";
	}

	@Override
	public void execute(String args) {
		String[] splitArgs = args.split(" ");
		
		if (splitArgs.length == 2) {
			int key = 0;
			try {
				key = Integer.parseInt(splitArgs[0]);
				Path path = validateAndGetPath(splitArgs[1]);
				MyFile file = fileReader.readFile(path);
				int chordKey = ChordState.chordHash(key);
				
				if (key < 0 || key >= ChordState.CHORD_SIZE) {
					throw new NumberFormatException();
				}
				AppConfig.chordState.putValue(chordKey, file);
			} catch (NumberFormatException e) {
				AppConfig.timestampedErrorPrint("Invalid key and value pair. Both should be ints. 0 <= key <= " + ChordState.CHORD_SIZE
						+ ". 0 <= value.");
			}catch (InvalidPathException e){
				AppConfig.timestampedErrorPrint("Invalid path: " + splitArgs[1]);
			}
		} else {
			AppConfig.timestampedErrorPrint("Invalid arguments for put");
		}

	}


	private Path validateAndGetPath(String location) throws InvalidPathException{
		Path path = Paths.get(absolutePath + location);
		if(Files.exists(path)){
			return path;
		}
		throw new InvalidPathException(location, "Path does not exist.");
	}

}
