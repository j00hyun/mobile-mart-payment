package com.automart.advice.exception;

public class ForbiddenSaveException extends RuntimeException {
    public ForbiddenSaveException() {
        super();
    }

    public ForbiddenSaveException(String message) {
        super(message);
    }

    public ForbiddenSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenSaveException(Throwable cause) {
        super(cause);
    }

    protected ForbiddenSaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
