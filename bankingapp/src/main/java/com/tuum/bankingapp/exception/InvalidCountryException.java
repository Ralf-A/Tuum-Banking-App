package com.tuum.bankingapp.exception;

public class InvalidCountryException extends RuntimeException{
    public InvalidCountryException(String message) {
        super(message);
    }
}
