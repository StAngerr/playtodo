package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    public static void writeToFile(String path, String fileName, File file) {
//        createIfNoFile();
//        FileWriter file = null;
//        file = new FileWriter(filePath);
//        file.write(users.toJSONString());
//        file.flush();
//        file.close();
    }

    public static File createFile(String path) {
        File file = new File(System.getProperty("user.dir") + path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            return null;
        }
        return file;
    }

}
