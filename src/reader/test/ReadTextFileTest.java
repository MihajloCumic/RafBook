package reader.test;

import data.file.MyFile;
import reader.Reader;
import reader.impl.FileReader;
import writer.Writer;
import writer.impl.FileWriter;

public class ReadTextFileTest {
    public static void main(String[] args) {
        Reader reader = new FileReader();
        Writer writer = new FileWriter();
        String location = "/home/cuma/Fakultet/letnji-semestar/konkurenti-distribuirani/domaci/projekat/KiDS-vezbe9/resources/";
        String file1 = "text-file1.txt";
        String file2 = "image1.jpg";

        MyFile myFile = reader.readFile(location + file2, "file1");
        writer.saveFile(location + "/saved", "savedFile", myFile);
        System.out.println(myFile);
    }
}
