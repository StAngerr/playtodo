package utils;

import utils.errorHandler.ErrorCreatingFile;
import utils.errorHandler.ErrorWhileReadingFile;
import utils.errorHandler.FileDoesNotExist;

import java.io.*;
import java.nio.file.Paths;

public class FileManager {
    public static File createFile(String path) throws ErrorCreatingFile {
        File file = new File(System.getProperty("user.dir") + adoptFilePathToSystem(path));
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new ErrorCreatingFile();
        }
        return file;
    }

    public static FileReader readFile(String path) throws FileNotFoundException {
        return new FileReader(adoptFilePathToSystem(path));
    }

    public static void saveToFile(String path, String data) throws IOException {
        createIfNoFile(path);
        FileWriter file;
        file = new FileWriter(path);
        file.write(data);
        file.flush();
        file.close();
    }

    public static void createIfNoFile(String path) throws IOException {
        File file = new File(adoptFilePathToSystem(path));
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public static byte[] fileToByteArray(String path) throws FileDoesNotExist, ErrorWhileReadingFile {
        if (!doesFileExist(path)) {
            throw new FileDoesNotExist(path);
        }
        byte[] fileBytes;
        try {
            fileBytes = java.nio.file.Files.readAllBytes( Paths.get(System.getProperty("user.dir") + adoptFilePathToSystem(path)));
            return fileBytes;
        } catch (IOException e) {
            throw new ErrorWhileReadingFile(path);
        }
    }

    public static String adoptFilePathToSystem(String path) {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            return path.replace('/', '\\');
        }
        return path;
    }

    public static boolean doesFileExist(String path) {
        File test = new File(System.getProperty("user.dir") + adoptFilePathToSystem(path));
        return test.exists();
    }
}
