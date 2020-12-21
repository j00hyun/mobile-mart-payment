package com.automart.exception;

public class SigninFailedException extends Throwable {
    public SigninFailedException() {
        super();
    }

    public SigninFailedException(String message) {
        super(message);
    }

    public SigninFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SigninFailedException(Throwable cause) {
        super(cause);
    }

    protected SigninFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
