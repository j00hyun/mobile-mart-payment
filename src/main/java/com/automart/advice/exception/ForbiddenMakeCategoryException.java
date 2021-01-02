package com.automart.advice.exception;

public class ForbiddenMakeCategoryException extends RuntimeException {
    public ForbiddenMakeCategoryException() {
        super();
    }

    public ForbiddenMakeCategoryException(String message) {
        super(message);
    }

    public ForbiddenMakeCategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenMakeCategoryException(Throwable cause) {
        super(cause);
    }

    protected ForbiddenMakeCategoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
