package com.automart.advice.exception;

public class SignInTypeErrorException extends RuntimeException {
    public SignInTypeErrorException() {
        super();
    }

    public SignInTypeErrorException(String message) {
        super(message);
    }

    public SignInTypeErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignInTypeErrorException(Throwable cause) {
        super(cause);
    }

    protected SignInTypeErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
