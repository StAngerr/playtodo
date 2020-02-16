package utils.errorHandler;

public class FileNameIsToLong extends ErrorHandler {
    public FileNameIsToLong() {
        super("File name is to long");
    }
    public FileNameIsToLong(String fileName) {
        super("File name is to long: " + fileName );
    }
}
