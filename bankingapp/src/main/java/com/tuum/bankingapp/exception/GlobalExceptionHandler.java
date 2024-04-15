package com.tuum.bankingapp.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;


@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception class for handling all exceptions in the application.
     * The class provides a method for handling all exceptions and returning a response entity with a message.
     */

    private static final String DOC_URL = "https://github.com/Ralf-A/Tuum-Banking-App/blob/main/README.md";

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String error = String.format("Method argument type mismatch: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String error = String.format("Missing servlet request parameter: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException e) {
        String error = String.format("No handler found: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException() {
        String error = String.format("Incorrect usage! Please check the documentation at %s", DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException e) {
        String error = String.format("Account not found: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException e) {
        String error = String.format("Insufficient funds: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(InvalidAccountException.class)
    public ResponseEntity<String> handleInvalidAccountException(InvalidAccountException e) {
        String error = String.format("Invalid account: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<String> handleInvalidAmountException(InvalidAmountException e) {
        String error = String.format("Invalid amount: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    
    @ExceptionHandler(InvalidCountryException.class)
    public ResponseEntity<String> handleInvalidCountryException(InvalidCountryException e) {
        String error = String.format("Invalid country: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(InvalidCurrencyException.class)
    public ResponseEntity<String> handleInvalidCurrencyException(InvalidCurrencyException e) {
        String error = String.format("Invalid currency: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(InvalidCustomerException.class)
    public ResponseEntity<String> handleInvalidCustomerException(InvalidCustomerException e) {
        String error = String.format("Invalid customer: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(InvalidDescriptionException.class)
    public ResponseEntity<String> handleInvalidDescriptionException(InvalidDescriptionException e) {
        String error = String.format("Invalid description: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(InvalidDirectionException.class)
    public ResponseEntity<String> handleInvalidDirectionException(InvalidDirectionException e) {
        String error = String.format("Invalid direction: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<String> handleInvalidTransactionException(InvalidTransactionException e) {
        String error = String.format("Invalid transaction: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<String> handleInvalidParameterException(InvalidParameterException e) {
        String error = String.format("Invalid parameter: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}


