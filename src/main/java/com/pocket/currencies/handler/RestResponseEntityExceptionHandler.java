package com.pocket.currencies.handler;

import com.pocket.currencies.currencies.exception.UpdateCurrenciesFailedException;
import com.pocket.currencies.pocket.exception.IncorrectInputDataException;
import com.pocket.currencies.registration.exception.EmailAlreadyConfirmedException;
import com.pocket.currencies.registration.exception.EmailIsNotValidException;
import com.pocket.currencies.registration.exception.TokenExpiredException;
import com.pocket.currencies.registration.exception.TokenIsNotValidException;
import com.pocket.currencies.users.exception.EmailAlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = EmailAlreadyConfirmedException.class)
    protected ResponseEntity<Object> handleEmailAlreadyConfirmer(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Email was already confirmed!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = EmailIsNotValidException.class)
    protected ResponseEntity<Object> handleEmailIsNotValid(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Email is not valid!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = TokenExpiredException.class)
    protected ResponseEntity<Object> handleTokenExpired(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Token is expired! Send registration request once again to receive new token.";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, request);
    }

    @ExceptionHandler(value = TokenIsNotValidException.class)
    protected ResponseEntity<Object> handleTokenIsNotValid(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Token is not valid!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = EmailAlreadyExistsException.class)
    protected ResponseEntity<Object> handleEmailAlreadyExists(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Account with this email already exists!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = IncorrectInputDataException.class)
    protected ResponseEntity<Object> handleIncorerectInputData(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Input data has been incorrect!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = UpdateCurrenciesFailedException.class)
    protected ResponseEntity<Object> handleUpdatedCurrenciesFailed(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Unexpected error during updating currencies!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
