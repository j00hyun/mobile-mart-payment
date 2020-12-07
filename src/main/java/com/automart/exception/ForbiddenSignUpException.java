package com.automart.exception;

public class ForbiddenSignUpException extends RuntimeException {
    public ForbiddenSignUpException() {
        super();
    }

    public ForbiddenSignUpException(String message) {
        super(message);
    }

    public ForbiddenSignUpException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenSignUpException(Throwable cause) {
        super(cause);
    }

    protected ForbiddenSignUpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
