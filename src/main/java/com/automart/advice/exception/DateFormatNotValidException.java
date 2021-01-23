package com.automart.advice.exception;

public class DateFormatNotValidException extends RuntimeException {
    public DateFormatNotValidException() {
        super();
    }

    public DateFormatNotValidException(String message) {
        super(message);
    }

    public DateFormatNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateFormatNotValidException(Throwable cause) {
        super(cause);
    }

    protected DateFormatNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
