package com.automart.advice.exception;

public class RequirePasswordChangeException extends RuntimeException {

    public RequirePasswordChangeException(String message) {
        super(message);
    }

    public RequirePasswordChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequirePasswordChangeException(Throwable cause) {
        super(cause);
    }

    protected RequirePasswordChangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
