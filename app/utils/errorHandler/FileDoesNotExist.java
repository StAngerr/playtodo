package utils.errorHandler;

public class FileDoesNotExist extends ErrorHandler {
    public FileDoesNotExist(String fileName) {
        super("File with path: " + fileName + " does not exist.", null);
    }
}
