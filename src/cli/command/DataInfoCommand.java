package cli.command;

import app.AppConfig;
import data.file.MyFile;

import java.util.Map;

public class DataInfoCommand implements CLICommand{
    @Override
    public String commandName() {
        return "data-info";
    }

    @Override
    public void execute(String args) {
        System.out.println("NodeId-> " + AppConfig.myServentInfo.getChordId());
        System.out.println("---------------");
        Map<Integer, MyFile> map = AppConfig.chordState.getValueMap();
        for(Map.Entry<Integer, MyFile> entry: map.entrySet()){
            System.out.println("Key-> " + entry.getKey() + " :::: Value" + entry.getValue().toString());
        }
    }
}
