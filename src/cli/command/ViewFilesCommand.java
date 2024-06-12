package cli.command;

import app.AppConfig;
import app.ChordState;
import app.ServentInfo;
import servent.message.AskViewFilesMessage;
import servent.message.util.MessageUtil;

public class ViewFilesCommand implements CLICommand{
    @Override
    public String commandName() {
        return "view-files";
    }

    @Override
    public void execute(String args) {
        if(args == null || args.isEmpty()){
            AppConfig.timestampedErrorPrint("view-files command must have argument.");
            return;
        }
        String[] splitArgs = args.split(":", 2);
        if(splitArgs.length != 2) {
            AppConfig.timestampedErrorPrint("Invalid arguments for view-files command.");
            return;
        }

        try{
            int port = Integer.parseInt(splitArgs[1]);
            if(port == AppConfig.myServentInfo.getListenerPort()){
                AppConfig.timestampedStandardPrint("On this node localhost:" + port + ":\n" + AppConfig.chordState.getValueMap().keySet().toString());
                return;
            }
            int chordId = ChordState.chordHash(port);
            ServentInfo nextNode = AppConfig.chordState.getNextNodeForKey(chordId);
            AskViewFilesMessage avfm = new AskViewFilesMessage(AppConfig.myServentInfo.getListenerPort(),nextNode.getListenerPort(), port + "");
            MessageUtil.sendMessage(avfm);
        }catch (NumberFormatException e){
            AppConfig.timestampedErrorPrint("Port must be a number.");
        }


    }
}
