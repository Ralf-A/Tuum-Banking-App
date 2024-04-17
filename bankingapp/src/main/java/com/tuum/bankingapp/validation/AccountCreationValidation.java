package com.tuum.bankingapp.validation;

import com.tuum.bankingapp.exception.InvalidCountryException;
import com.tuum.bankingapp.exception.InvalidCurrencyException;
import com.tuum.bankingapp.exception.InvalidCustomerException;
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

    // Allowed currencies list from application.properties
    @Value("#{'${allowed.currencies}'.split(',')}")
    private List<String> allowedCurrencies;

    /**
     * Validates account details, for country, customer ID and currencies validation
     * @param customerId Customer ID
     * @param country    Country
     * @param currencies List of currencies
     */
    public void validateAccount(Long customerId, String country, List<String> currencies) {
        log.info("Validating account for customer ID: {}", customerId);
        isValidCountry(country);
        isValidCustomerId(customerId);
        for (String currency : currencies) {
            isValidCurrency(currency);
        }
        log.info("Account validated successfully");
    }

    /**
     * Validates the currency from list of allowed currencies
     * @param currency Currency
     */
    public void isValidCurrency(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            log.error("Currency is null or empty: {}", currency);
            throw new InvalidCurrencyException("Invalid currency: " + currency);
        }
        if (!allowedCurrencies.contains(currency)) {
            log.error("Currency is not in the list of allowed currencies: {}", currency);
            throw new InvalidCurrencyException("Currency is not in the list of allowed currencies: " + currency + ", allowed currencies: " + allowedCurrencies);
        }
    }

    /**
     * Validates the customer ID and checks if it already exists
     * @param customerId Customer ID
     */
    public void isValidCustomerId(Long customerId) {
        if (customerId == null || customerId <= 0) {
            log.error("Customer ID is null or invalid: {}", customerId);
            throw new InvalidCustomerException("Invalid customer ID: " + customerId);
        }
        List<Account> accounts = accountRepository.findAccountsByCustomerId(customerId);
        if (!accounts.isEmpty()) {
            log.error("Customer ID already exists: {}", customerId);
            throw new InvalidCustomerException("A customer already exists with customer ID of: " + customerId);
        }
    }

    /**
     * Validates the country so that it is not null, empty, doesn't contain integers or not longer than 3 characters
     * @param country Country
     */
    public void isValidCountry(String country) {
        if (country == null || country.trim().isEmpty()) {
            log.error("Country is null or empty: {}", country);
            throw new InvalidCountryException("Country is null or empty");
        }
        // Regular expression to match any digit
        Pattern pattern = Pattern.compile(".*\\d.*");
        if (pattern.matcher(country).find()) {
            log.error("Country contains integers: {}", country);
            throw new InvalidCountryException("Country contains integers: " + country);
        }
        if (country.length() > 3) {
            log.error("Country code is longer than 3 characters: {}", country);
            throw new InvalidCountryException("Country code is longer than 3 characters: " + country);
        }
    }
}

