package cli.command;

import app.AppConfig;
import data.result.DeleteFIleResult;

public class DeleteFileCommand implements CLICommand{
    @Override
    public String commandName() {
        return "delete";
    }

    @Override
    public void execute(String args) {
        if(args == null || args.isEmpty()){
            AppConfig.timestampedErrorPrint("Invalid input for delete command.");
            return;
        }
//        int fileChordId = Math.abs(args.hashCode());
        int key = Integer.parseInt(args);
        DeleteFIleResult res = AppConfig.chordState.deleteFile(key);
        if(res.getResStatus() == -2){
            AppConfig.timestampedStandardPrint("Please wait...");
            return;
        }
        if(res.getResStatus() == -1){
            AppConfig.timestampedStandardPrint("No file with name: " + args);
            return;
        }
        if(res.getResStatus() == 1){
            AppConfig.timestampedStandardPrint("Deleted file with name: " + res.getFileName() + " from node: " + res.getNodePort());
            return;
        }
        AppConfig.timestampedErrorPrint("Error in DeleteCommand.");
    }
}
