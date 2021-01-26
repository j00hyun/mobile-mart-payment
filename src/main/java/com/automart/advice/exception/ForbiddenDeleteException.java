package com.automart.advice.exception;

public class ForbiddenDeleteException extends RuntimeException {
    public ForbiddenDeleteException() {
        super();
    }

    public ForbiddenDeleteException(String message) {
        super(message);
    }

    public ForbiddenDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenDeleteException(Throwable cause) {
        super(cause);
    }

    protected ForbiddenDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
