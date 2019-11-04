package utils.errorHandler;

public class NoPermission extends ErrorHandler {
    public NoPermission() {
        super("User have no permissions for this action.");
    }
}
