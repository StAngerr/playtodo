package utils.errorHandler;

public class ErrorCreatingFile extends ErrorHandler {
    public ErrorCreatingFile(Throwable e) {
        super("Error create a file.", e);
    }
}
