package utils.errorHandler;

public class InvalidRequestData extends ErrorHandler {
    public InvalidRequestData() {
        super("No request body or bad data format.", null);
    }
}
