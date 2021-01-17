package com.automart.advice;

import com.automart.advice.exception.ForbiddenSignUpException;
import com.automart.advice.exception.NotEnoughStockException;
import com.automart.advice.exception.NotFoundUserException;
import com.automart.advice.exception.RequirePasswordChangeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(ForbiddenSignUpException.class)
    protected ResponseEntity<String> forbiddenSignUp (ForbiddenSignUpException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundUserException.class)
    protected ResponseEntity<String> signInFailed (NotFoundUserException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(NotEnoughStockException.class)
    protected ResponseEntity<String> notEnoughStock (NotEnoughStockException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }

    @ExceptionHandler(RequirePasswordChangeException.class)
    protected ResponseEntity<String> requirePasswordChange (RequirePasswordChangeException e) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
    }
}
