package com.tuum.bankingapp.validation;

import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.repository.AccountRepository;
import com.tuum.bankingapp.service.AccountService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class AccountCreationValidation {
    Logger log = org.slf4j.LoggerFactory.getLogger(AccountService.class);
    @Autowired
    private AccountRepository accountRepository;

    private static final List<String> ALLOWED_CURRENCIES = Arrays.asList("EUR", "SEK", "GBP", "USD");

    public boolean isValidCurrency(List<String> currencies) {
        if (currencies == null || currencies.isEmpty()) {
            log.error("Currency is null or empty: {}", currencies);
            return false;
        }
        List<String> invalidCurrencies = currencies.stream()
                .filter(currency -> !ALLOWED_CURRENCIES.contains(currency))
                .collect(Collectors.toList());
        if (!invalidCurrencies.isEmpty()) {
            log.error("Invalid currencies: {}", invalidCurrencies);
            return false;
        }
        return true;
    }

    public boolean isValidCustomerId(Long customerId) {
        if (customerId == null || customerId <= 0) {
            log.error("Customer ID is null or invalid: {}", customerId);
            return false;
        }
        List<Account> accounts = accountRepository.findAccountsByCustomerId(customerId);
        if (!accounts.isEmpty()) {
            log.error("Customer ID already exists: {}", customerId);
            return false;
        }
        return true;
    }


    public boolean isValidCountry(String country) {
        if (country == null || country.trim().isEmpty()) {
            log.info("Country is null or empty: {}", country);
            return false;
        }
        // Regular expression to match any digit
        Pattern pattern = Pattern.compile(".*\\d.*");
        if (pattern.matcher(country).find()) {
            log.info("Country contains integers: {}", country);
            return false;
        }
        return true;
    }
}

