package com.tuum.bankingapp.validation;

import java.util.List;

public interface AccountValidation {
    void isValidCurrency(String currency);
    void isValidCustomerId(Long customerId);
    void isValidCountry(String country);
    void validateAccount(Long customerId, String country, List<String> currencies);
}

