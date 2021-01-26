package com.automart.advice.exception;

public class SessionUnstableException extends RuntimeException {
    public SessionUnstableException() {
        super();
    }

    public SessionUnstableException(String message) {
        super(message);
    }

    public SessionUnstableException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionUnstableException(Throwable cause) {
        super(cause);
    }

    protected SessionUnstableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
