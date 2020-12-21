package com.automart.exception;

public class ForbiddenMakeProductException extends RuntimeException {
    public ForbiddenMakeProductException() {
        super();
    }

    public ForbiddenMakeProductException(String message) {
        super(message);
    }

    public ForbiddenMakeProductException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenMakeProductException(Throwable cause) {
        super(cause);
    }

    protected ForbiddenMakeProductException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
