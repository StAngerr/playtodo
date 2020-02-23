package utils.errorHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler extends Exception {

    public ErrorHandler(String msg, Throwable cause) {
        super(msg, cause);
    }
}
