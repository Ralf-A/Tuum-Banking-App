package com.tuum.bankingapp.exception;

public class InvalidAccountException extends RuntimeException{
    public InvalidAccountException(String message) {
        super(message);
    }
}
