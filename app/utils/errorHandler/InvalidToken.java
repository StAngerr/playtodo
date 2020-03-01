package utils.errorHandler;

public class InvalidToken extends ErrorHandler {
    public InvalidToken(Throwable e) {
        super("Invalid token.", e);
    }
    public InvalidToken() {
        super("Invalid token.");
    }
}
