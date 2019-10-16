package yapi.exceptions;

public class NoStringException extends YAPIException {

    public NoStringException() {
        super();
    }

    public NoStringException(String message) {
        super(message);
    }

    public NoStringException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoStringException(Throwable cause) {
        super(cause);
    }

    protected NoStringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
