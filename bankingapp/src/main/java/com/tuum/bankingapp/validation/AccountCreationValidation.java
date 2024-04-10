package com.tuum.bankingapp.validation;

import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountCreationValidation {
    @Autowired
    private AccountRepository accountRepository;

    private static final List<String> ALLOWED_CURRENCIES = Arrays.asList("EUR", "SEK", "GBP", "USD");

    public boolean isValidCurrency(List<String> currencies) {
        return currencies != null && !currencies.isEmpty() && currencies.stream()
                .allMatch(currency -> ALLOWED_CURRENCIES.contains(currency));
    }

    public boolean isValidCustomerId(Long customerId) {
        if (customerId == null || customerId <= 0) {
            return false;
        }
        // Assuming that the method findAccountsByCustomerId should return a list of accounts
        List<Account> existingAccounts = accountRepository.findAccountsByCustomerId(customerId);
        return existingAccounts.isEmpty(); // Customer ID is valid if no existing accounts are found
    }

    public boolean isValidCountry(String country) {
        // You can enhance this by checking against a list of valid countries if needed
        return country != null && !country.trim().isEmpty();
    }
}

