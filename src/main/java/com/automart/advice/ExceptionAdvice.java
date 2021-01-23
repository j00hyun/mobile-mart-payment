package com.automart.advice;

import com.automart.advice.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(NotEnoughStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleNotEnoughStockException (NotEnoughStockException e) {
        return ErrorResponse.ErrorOf(400, e.getMessage());
    }

    @ExceptionHandler(SMSException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse handleSMSException (SMSException e) {
        return ErrorResponse.ErrorOf(500, e.getMessage());
    }

    @ExceptionHandler(DuplicateDataException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ErrorResponse handleDuplicateDataException (DuplicateDataException e) {
        return ErrorResponse.ErrorOf(403, e.getMessage());
    }

    @ExceptionHandler(NotFoundDataException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleNotFoundDataException (NotFoundDataException e) {
        return ErrorResponse.ErrorOf(404, e.getMessage());
    }

    @ExceptionHandler(SessionUnstableException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponse handleNotFoundDataException (SessionUnstableException e) {
        return ErrorResponse.ErrorOf(401, e.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleMaxUploadSizeExceedException (MaxUploadSizeExceedException e) {
        return ErrorResponse.ErrorOf(400, e.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse handleMultipartException (MultipartException e) {
        return ErrorResponse.ErrorOf(500, e.getMessage());
    }

    @ExceptionHandler(DateFormatNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleDateFormatNotValidException (DateFormatNotValidException e) {
        return ErrorResponse.ErrorOf(400, e.getMessage());

    @ExceptionHandler(OAuth2AuthenticationProcessingException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ErrorResponse handleOAuth2AuthenticationProcessingException (OAuth2AuthenticationProcessingException e) {
        return ErrorResponse.ErrorOf(403, e.getMessage());
    }

    // DTO 검사
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        /* FieldError를 받아온다*/
        List<ErrorResponse.FieldError> fieldErrors = getFieldErrors(e.getBindingResult());
        return ErrorResponse.FieldErrorOf(400, "유효한 입력값이 아닙니다.", fieldErrors);
    }

    private List<ErrorResponse.FieldError> getFieldErrors(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();

        return errors.parallelStream().map(error -> ErrorResponse.FieldError.builder()
                .field(error.getField())
                .value((String) error.getRejectedValue())
                .reason(error.getDefaultMessage())
                .build())
                .collect(Collectors.toList());
    }

}
