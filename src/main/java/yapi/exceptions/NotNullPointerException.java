package yapi.exceptions;

public class NotNullPointerException extends YAPIException {

    public NotNullPointerException() {
        super();
    }

    public NotNullPointerException(String message) {
        super(message);
    }

    public NotNullPointerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotNullPointerException(Throwable cause) {
        super(cause);
    }

    protected NotNullPointerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
