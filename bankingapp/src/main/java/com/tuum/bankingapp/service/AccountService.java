package com.tuum.bankingapp.service;

import com.tuum.bankingapp.exception.AccountNotFoundException;
import com.tuum.bankingapp.exception.InvalidCountryException;
import com.tuum.bankingapp.exception.InvalidCurrencyException;
import com.tuum.bankingapp.exception.InvalidCustomerException;
import com.tuum.bankingapp.messaging.MessagePublisher;
import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.model.AccountBalance;
import com.tuum.bankingapp.model.Balance;
import com.tuum.bankingapp.repository.AccountBalanceRepository;
import com.tuum.bankingapp.repository.AccountRepository;
import com.tuum.bankingapp.repository.BalanceRepository;
import com.tuum.bankingapp.validation.AccountCreationValidation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;



@Service
public class AccountService {
    Logger log = org.slf4j.LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private AccountBalanceRepository accountBalanceRepository;
    @Autowired
    private MessagePublisher messagePublisher;
    @Autowired
    private AccountCreationValidation accountCreationValidation;

    public Account getAccountById(Long accountId) {
        log.info("Finding account by ID: {}", accountId);
        Account account = accountRepository.findAccountById(accountId);
        if (accountId == null || account == null) {
            log.info("Account not found");
            throw new AccountNotFoundException("Account not found");
        }
        // Fetch balances for the account
        List<Balance> balances = getBalancesForAccount(accountId);
        account.setBalances(balances);
        log.info("Account found: {}", account);
        return account;
    }


    private List<Balance> getBalancesForAccount(Long accountId) {
        log.info("Finding balances for account: {}", accountId);
        List<Long> balanceIds = accountBalanceRepository.findBalanceIdsByAccountId(accountId);
        log.info("Found balance IDs: {}", balanceIds);
        return balanceIds.stream()
                .map(balanceRepository::findBalanceById)
                .collect(Collectors.toList());
    }

    @Transactional
    public Account createAccount(String customerId, String country, List<String> currencies) {
        // Create a new account object
        validateAccount(customerId, country, currencies);
        Account account = new Account();
        account.setCustomerId(Long.parseLong(customerId));
        account.setCountry(country);
        account.setBalances(new ArrayList<>());

        // Validate account
        log.info("Creating account, validating account details for account");


        // Insert account into the database
        accountRepository.insertAccount(account);
        Long accountId = account.getAccountId();

        // Initialize balances for the specified currencies and link them to the account
        for (String currency : currencies) { // Use the specified list of currencies
            Balance balance = new Balance(null, currency, BigDecimal.ZERO);
            balanceRepository.insertBalance(balance);
            Long balanceId = balance.getBalanceId(); // Ensure this is being retrieved after insertion
            accountBalanceRepository.insertAccountBalance(accountId, balanceId);
            account.getBalances().add(new Balance(balanceId, currency, BigDecimal.ZERO));
        }

        // Publish account creation event
        messagePublisher.publishAccountEvent(account);
        log.info("Account created successfully: {}", accountId);
        return account;
    }



    private void validateAccount(String customerId, String country, List<String> currencies) {
        // Validate country
        log.info("Validating account country: {}", country);
        if (!accountCreationValidation.isValidCountry(country)) {
            throw new InvalidCountryException("Invalid country");
        }

        // Validate customer ID
        log.info("Validating account customer ID: {}", customerId);
        Long customerIdLong;
        try {
            customerIdLong = Long.parseLong(customerId);
        } catch (NumberFormatException e) {
            throw new InvalidCustomerException("Invalid customer ID format");
        }
        if (!accountCreationValidation.isValidCustomerId(customerIdLong)) {
            throw new InvalidCustomerException("Invalid customer ID");
        }

        // Validate currencies
        log.info("Validating account currencies: {}", currencies);
        if (currencies == null || currencies.isEmpty()) {
            throw new InvalidCurrencyException("Currency list is empty or null");
        }
        if (!accountCreationValidation.isValidCurrency(currencies)) {
            throw new InvalidCurrencyException("Invalid currency");
        }
    }


}

