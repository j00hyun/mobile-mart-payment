package com.automart.advice;


import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {

    private int code;
    private String message;
    private List<FieldError> errors;

    @Builder
    public ErrorResponse(int code, String message, List<FieldError> errors) {
        this.code = code;
        this.message = message;
        this.errors = initErrors(errors);
    }

    private List<FieldError> initErrors(List<FieldError> errors) {
        return (errors == null) ? new ArrayList<>() : errors;
    }

    @Getter
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        @Builder
        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

    }

    // 일반 Exception
    public static ErrorResponse ErrorOf(int code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }

    // DTO Exception
    public static ErrorResponse FieldErrorOf(int code, String message, List<FieldError> errors) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .errors(errors)
                .build();
    }

}
