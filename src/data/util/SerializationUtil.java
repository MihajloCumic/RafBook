package data.util;

import java.io.*;
import java.util.Base64;

public class SerializationUtil {

    public static String serialize(Object object) throws IOException {
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)){
            objectOutputStream.writeObject(object);
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        }
    }

    public static Object deserialize(String str) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(str);
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))){
            return objectInputStream.readObject();
        }
    }
}
