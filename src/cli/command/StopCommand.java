package cli.command;

import app.AppConfig;
import cli.CLIParser;
import heartbeat.Heartbeat;
import servent.SimpleServentListener;

public class StopCommand implements CLICommand {

	private CLIParser parser;
	private SimpleServentListener listener;
	private Heartbeat heartbeat;
	
	public StopCommand(CLIParser parser, SimpleServentListener listener, Heartbeat heartbeat) {
		this.parser = parser;
		this.listener = listener;
		this.heartbeat = heartbeat;
	}
	
	@Override
	public String commandName() {
		return "stop";
	}

	@Override
	public void execute(String args) {
		AppConfig.timestampedStandardPrint("Stopping...");
		parser.stop();
		listener.stop();
		heartbeat.stop();
	}

}
