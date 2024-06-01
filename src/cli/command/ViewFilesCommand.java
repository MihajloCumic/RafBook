package cli.command;

import app.AppConfig;

public class ViewFilesCommand implements CLICommand{
    @Override
    public String commandName() {
        return "view-files";
    }

    @Override
    public void execute(String args) {
        String[] splitArgs = args.split(":", 2);
        if(splitArgs.length != 2) AppConfig.timestampedErrorPrint("Invalid arguments for view-files command.");
        try{
            Integer port = Integer.parseInt(splitArgs[1]);
            if(port == AppConfig.myServentInfo.getListenerPort()){
                AppConfig.timestampedStandardPrint("To sam ja.");
                return;
            }
            AppConfig.timestampedStandardPrint("Nisam ja.");
        }catch (NumberFormatException e){
            AppConfig.timestampedErrorPrint("Port must be a number.");
        }


    }
}
