package data.test;

import data.enums.FileType;
import data.file.MyFile;
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
            System.out.println(myFile1.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
