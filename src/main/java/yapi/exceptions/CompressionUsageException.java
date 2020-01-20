package yapi.exceptions;

public class CompressionUsageException extends YAPIException {

    public CompressionUsageException() {
        super();
    }

    public CompressionUsageException(String message) {
        super(message);
    }

    public CompressionUsageException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompressionUsageException(Throwable cause) {
        super(cause);
    }

    protected CompressionUsageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}