package yapi.exceptions;

import yapi.exceptions.YAPIException;

public class StateMachineException extends YAPIException {

    public StateMachineException() {
        super();
    }

    public StateMachineException(String message) {
        super(message);
    }

    public StateMachineException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateMachineException(Throwable cause) {
        super(cause);
    }

    protected StateMachineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
