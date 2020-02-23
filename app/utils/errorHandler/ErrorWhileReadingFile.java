package utils.errorHandler;

public class ErrorWhileReadingFile extends ErrorHandler {
    public ErrorWhileReadingFile(String file, Throwable e) {
        super("Error while reading file:" + file, e);
    }
}
