package utils.errorHandler;

public class ErrorHandler extends Exception {

    public ErrorHandler(String msg) {
        super(msg);
    }

    public ErrorHandler(String msg, Throwable cause) {
        super(msg, cause);
    }
}
