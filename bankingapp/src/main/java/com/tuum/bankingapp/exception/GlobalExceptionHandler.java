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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllOtherExceptions(Exception e) {
        String error = String.format("An unexpected error occurred: %s. Please check the documentation at %s", e.getMessage(), DOC_URL);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

}


