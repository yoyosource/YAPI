package yapi.exceptions;

public class JSONException extends YAPIException {

    public JSONException() {
        super();
    }

    public JSONException(String message) {
        super(message);
    }

    public JSONException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSONException(Throwable cause) {
        super(cause);
    }

    protected JSONException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
