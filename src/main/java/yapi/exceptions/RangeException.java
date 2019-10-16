package yapi.exceptions;

public class RangeException extends YAPIException {

    public RangeException() {
        super();
    }

    public RangeException(String message) {
        super(message);
    }

    public RangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RangeException(Throwable cause) {
        super(cause);
    }

    protected RangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
