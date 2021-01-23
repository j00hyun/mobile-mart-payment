package com.automart.advice.exception;

public class MaxUploadSizeExceedException extends RuntimeException {
    public MaxUploadSizeExceedException() {
        super();
    }

    public MaxUploadSizeExceedException(String message) {
        super(message);
    }

    public MaxUploadSizeExceedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaxUploadSizeExceedException(Throwable cause) { super(cause); }

    protected MaxUploadSizeExceedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
