package com.tuum.bankingapp.validation;

import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.repository.AccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class for validating account creation related input parameters
 */
@Component
public class AccountCreationValidation implements AccountValidation {
    Logger log = org.slf4j.LoggerFactory.getLogger(AccountCreationValidation.class);

    // Constructor and autowiring the dependencies
    private final AccountRepository accountRepository;
    @Autowired
    public AccountCreationValidation(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Value("#{'${allowed.currencies}'.split(',')}")
    private List<String> allowedCurrencies;

    /**
     * Validates the currency
     * @param currency Currency
     * @return True if the currency is valid, meaning included in the allowed countries list, false otherwise
     */
    public boolean isValidCurrency(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            log.error("Currency is null or empty: {}", currency);
            return false;
        }
        if (!allowedCurrencies.contains(currency)) {
            log.error("Invalid currency: {}", currency);
            return false;
        }
        return true;
    }

    /**
     * Validates the customer ID
     * @param customerId Customer ID
     * @return True if the customer ID is valid, meaning not null and not already existing, false otherwise
     */
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

    /**
     * Validates the country
     * @param country Country
     * @return True if the country is valid, meaning not null, not empty and not containing any digits, false otherwise
     */
    public boolean isValidCountry(String country) {
        if (country == null || country.trim().isEmpty()) {
            log.error("Country is null or empty: {}", country);
            return false;
        }
        // Regular expression to match any digit
        Pattern pattern = Pattern.compile(".*\\d.*");
        if (pattern.matcher(country).find()) {
            log.error("Country contains integers: {}", country);
            return false;
        }
        return true;
    }
}

