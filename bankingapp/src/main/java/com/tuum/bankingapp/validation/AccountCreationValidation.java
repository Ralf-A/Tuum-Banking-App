package com.tuum.bankingapp.validation;

import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AccountCreationValidation {
    @Autowired
    private AccountRepository accountRepository;


    public boolean isValidCurrency(List<String> currencies) {
        List<String> allowedCurrencies = Arrays.asList("EUR", "SEK", "GBP", "USD");
        return currencies.stream().allMatch(allowedCurrencies::contains);
    }
    public boolean isValidCustomerId(Long customerId) {
        if (customerId == null || customerId <= 0) {
            return false;
        }
        Account existingAccount = accountRepository.findAccountsByCustomerId(customerId);
        return existingAccount == null;
    }

    public boolean isValidCountry(String country) {
        return country != null && !country.trim().isEmpty();
        // For more robust validation, check against a list of ISO country codes
    }

}
