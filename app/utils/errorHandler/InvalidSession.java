package utils.errorHandler;

public class InvalidSession extends ErrorHandler {
    public InvalidSession(Throwable e) {
        super("Invalid session.", e);
    }

    public InvalidSession() {
        super("Invalid session.");
    }
}
