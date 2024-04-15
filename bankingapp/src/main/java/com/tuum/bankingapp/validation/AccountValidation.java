package com.tuum.bankingapp.validation;

public interface AccountValidation {
    boolean isValidCurrency(String currency);
    boolean isValidCustomerId(Long customerId);
    boolean isValidCountry(String country);
}

