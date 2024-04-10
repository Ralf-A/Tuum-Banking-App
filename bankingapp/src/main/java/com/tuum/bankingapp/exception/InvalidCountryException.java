package com.tuum.bankingapp.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class InvalidCountryException extends RuntimeException{
    public InvalidCountryException(String message) {
        super(message);
    }
}
