package com.automart.advice.exception;

public class MultipartException extends RuntimeException {
    public MultipartException() {
        super();
    }

    public MultipartException(String message) {
        super(message);
    }

    public MultipartException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipartException(Throwable cause) {
        super(cause);
    }

    protected MultipartException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
