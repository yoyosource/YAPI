package yapi.exceptions;

public class WindowCreatorException extends YAPIException {

    public WindowCreatorException() {
        super();
    }

    public WindowCreatorException(String message) {
        super(message);
    }

    public WindowCreatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public WindowCreatorException(Throwable cause) {
        super(cause);
    }

    protected WindowCreatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
