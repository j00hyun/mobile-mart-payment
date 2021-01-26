package com.automart.advice.exception;

public class DuplicateDataException extends RuntimeException {
    public DuplicateDataException() {
        super();
    }

    public DuplicateDataException(String message) {
        super(message);
    }

    public DuplicateDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateDataException(Throwable cause) {
        super(cause);
    }

    protected DuplicateDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
