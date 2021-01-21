package com.automart.advice.exception;

public class ForbiddenSaveProductException extends RuntimeException {
    public ForbiddenSaveProductException() {
        super();
    }

    public ForbiddenSaveProductException(String message) {
        super(message);
    }

    public ForbiddenSaveProductException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenSaveProductException(Throwable cause) {
        super(cause);
    }

    protected ForbiddenSaveProductException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
