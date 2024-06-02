package data.test;

import data.enums.FileType;
import data.file.MyFile;
import data.result.GetResult;
import data.util.SerializationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SerializationTest {
    public static void main(String[] args) {
        Path path = Paths.get("/home/cuma/Fakultet/letnji-semestar/konkurenti-distribuirani/domaci/projekat/KiDS-vezbe9/resources/text-file1.txt");
        try {
            byte[] content = Files.readAllBytes(path);
            MyFile myFile = new MyFile(content, "text1", FileType.TYPE_TEXT, false);
            String str = SerializationUtil.serialize(myFile);
            System.out.println(str);
            MyFile myFile1 = (MyFile) SerializationUtil.deserialize(str);
            System.out.println(myFile1.getExtensionAsString());
            System.out.println("Test2::::::::::::::::::::::");
            GetResult getResult = new GetResult(-1, null);
            String str1 = SerializationUtil.serialize(getResult);
            System.out.println(str1);
            GetResult getResult1 = (GetResult) SerializationUtil.deserialize(str1);
            System.out.println(getResult1.getResStatus());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
