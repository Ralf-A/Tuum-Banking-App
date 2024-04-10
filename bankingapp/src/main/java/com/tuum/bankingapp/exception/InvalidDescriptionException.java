package com.tuum.bankingapp.exception;

public class InvalidDescriptionException extends RuntimeException{
    public InvalidDescriptionException(String message) {
        super(message);
    }
}
