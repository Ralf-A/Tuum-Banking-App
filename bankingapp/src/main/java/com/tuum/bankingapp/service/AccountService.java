package com.tuum.bankingapp.service;

import com.tuum.bankingapp.exception.AccountNotFoundException;
import com.tuum.bankingapp.exception.InvalidCountryException;
import com.tuum.bankingapp.exception.InvalidCurrencyException;
import com.tuum.bankingapp.exception.InvalidCustomerException;
import com.tuum.bankingapp.messaging.MessagePublisher;
import com.tuum.bankingapp.model.Account;
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
        if (account == null) {
            log.error("Account not found for ID: {}", accountId);
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
    public Account createAccount(Long customerId, String country, List<String> currencies) {
        validateAccount(customerId, country, currencies);
        Account account = new Account();
        account.setCustomerId(customerId);
        account.setCountry(country);
        account.setBalances(new ArrayList<>());

        log.info("Creating account for customer ID: {}", customerId);

        accountRepository.insertAccount(account);
        Long accountId = account.getAccountId();

        for (String currency : currencies) {
            Balance balance = new Balance(null, currency, BigDecimal.ZERO);
            balanceRepository.insertBalance(balance);
            Long balanceId = balance.getBalanceId();
            accountBalanceRepository.insertAccountBalance(accountId, balanceId);
            account.getBalances().add(new Balance(balanceId, currency, BigDecimal.ZERO));
        }

        messagePublisher.publishAccountEvent(account);
        log.info("Account created successfully: {}", account);
        return account;
    }

    private void validateAccount(Long customerId, String country, List<String> currencies) {
        log.info("Validating account for customer ID: {}", customerId);
        if (!accountCreationValidation.isValidCountry(country)) {
            throw new InvalidCountryException("Invalid country: " + country);
        }

        if (!accountCreationValidation.isValidCustomerId(customerId)) {
            throw new InvalidCustomerException("Invalid customer ID: " + customerId);
        }

        for (String currency : currencies) {
            if (!accountCreationValidation.isValidCurrency(currency)) {
                throw new InvalidCurrencyException("Invalid currency: " + currency);
            }
        }
    }
}


