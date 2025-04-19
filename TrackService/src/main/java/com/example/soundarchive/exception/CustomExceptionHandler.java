package com.example.soundarchive.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Error> handleMethodArgumentNotValidException(HttpServletRequest request,
                                                                       MethodArgumentNotValidException e) {

        String errors = e.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        Error error = new Error(ErrorType.validationErrorType, errors);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Error> handleMethodArgumentTypeMismatchException(HttpServletRequest request,
                                                                           MethodArgumentTypeMismatchException e) {

        Error error = new Error(ErrorType.validationErrorType, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<Error> handleDataNotFoundException(HttpServletRequest request, DataNotFoundException e) {

        Error error = new Error(ErrorType.dataNotFoundErrorType, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Error> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException e) {

        Error error = new Error(ErrorType.badRequestErrorType, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {JsonProcessingException.class})
    public ResponseEntity<Error> handleJsonProcessingException(HttpServletRequest request, JsonProcessingException e) {

        Error error = new Error(ErrorType.internalServerErrorType, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Error> handleRuntimeException(HttpServletRequest request, RuntimeException e) {

        Error error = new Error(ErrorType.internalServerErrorType, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {InternalServerErrorException.class})
    public ResponseEntity<Error> handleInternalServerErrorException(HttpServletRequest request, InternalServerErrorException e) {

        Error error = new Error(ErrorType.internalServerErrorType, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {java.lang.Exception.class})
    public ResponseEntity<Error> handleException(HttpServletRequest request, java.lang.Exception e) {
        String message;

        if (e instanceof NullPointerException) {
            if (e.getCause() != null) {
                message = e.getCause().getMessage();
            } else {
                message = "NullPointerException occurred.";
            }
        } else {
            message = e.getMessage();
        }

        Error error = new Error(ErrorType.internalServerErrorType, message);

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {DuplicateDataException.class})
    public ResponseEntity<Error> handleDuplicateDataException(HttpServletRequest request, DuplicateDataException e) {

        Error error = new Error(ErrorType.duplicatedDataErrorType, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    loggingService.logExceptionMessage(ErrorType.duplicatedDataErrorType,
//            e.getMessage(), ExceptionUtils.getStackTrace(e),
//            HttpStatus.INTERNAL_SERVER_ERROR.toString(), request);
}
