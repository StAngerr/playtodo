package utils.errorHandler;

public class ErrorReadingUserStorage extends ErrorHandler {
    public ErrorReadingUserStorage(Throwable e) {
        super("Error while trying to read user storage.", e);
    }
}
