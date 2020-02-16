package utils.errorHandler;

public class ErrorWhileReadingFile extends ErrorHandler {
    public ErrorWhileReadingFile(String file) {
        super("Error while reading file:" + file);
    }
}
