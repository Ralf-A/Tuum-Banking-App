package com.tuum.bankingapp.exception;

public class InsufficentFundsException extends RuntimeException{
    public InsufficentFundsException(String message) {
        super(message);
    }
}
