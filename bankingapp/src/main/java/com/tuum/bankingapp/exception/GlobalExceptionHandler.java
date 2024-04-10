package com.tuum.bankingapp.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String DOC_URL = "https://github.com/Ralf-A/Tuum-Banking-App/blob/main/README.md";

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String error = String.format("An unexpected error occurred: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllOtherExceptions(Exception e) {
        String error = String.format("An unexpected error occurred: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}


