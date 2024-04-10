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

    public boolean isValidCustomerId(Long accountId) {
        if (accountId == null || accountId <= 0) {
            return false;
        }
        Account existingAccounts = accountRepository.findAccountById(accountId);
        if (existingAccounts == null) {
            return false;
        }
        return true;
    }


    public boolean isValidCountry(String country) {
        // You can enhance this by checking against a list of valid countries if needed
        return country != null && !country.trim().isEmpty();
    }
}

