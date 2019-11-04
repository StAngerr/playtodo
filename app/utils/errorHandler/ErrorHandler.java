package utils.errorHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler extends Exception {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    final String logPrefix = "CUSTOM LOG: ";
    public ErrorHandler(String msg) {
        super(msg);
        logError(msg);
    }

    private void logError(String msg) {
        log.info(logPrefix + msg);
    }
}
