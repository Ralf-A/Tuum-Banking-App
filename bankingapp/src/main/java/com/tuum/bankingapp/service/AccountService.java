package com.tuum.bankingapp.service;

import com.tuum.bankingapp.exception.AccountNotFoundException;
import com.tuum.bankingapp.exception.InvalidCountryException;
import com.tuum.bankingapp.exception.InvalidCurrencyException;
import com.tuum.bankingapp.exception.InvalidCustomerException;
import com.tuum.bankingapp.messaging.MessagePublisher;
import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.model.Balance;
import com.tuum.bankingapp.repository.AccountRepository;
import com.tuum.bankingapp.repository.BalanceRepository;
import com.tuum.bankingapp.validation.AccountCreationValidation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class AccountService {
    Logger log = org.slf4j.LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private MessagePublisher messagePublisher;

    @Autowired
    private AccountCreationValidation accountCreationValidation;

    public Account getAccountById(Long accountId) {
        log.info("Getting account by ID: {}", accountId);
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            log.info("Account not found with id: {}", accountId);
            throw new AccountNotFoundException("Account not found with id: " + accountId);
        }
        account.setBalances(balanceRepository.findBalancesByAccountId(accountId));
        log.info("Account found: {}", account);
        return account;
    }

    public Account createAccount(Account account) throws InvalidCurrencyException, InvalidCustomerException {
        log.info("Creating account: {}", account);
        if (account.getBalances() == null) {
            account.setBalances(new ArrayList<>());
        }
        if (!accountCreationValidation.isValidCurrency(account.getBalances().stream().map(Balance::getCurrency).collect(Collectors.toList()))) {
            log.info("Invalid currency provided.");
            throw new InvalidCurrencyException("Invalid currency provided.");
        }
        if (!accountCreationValidation.isValidCustomerId(account.getCustomerId())) {
            log.info("Invalid customer ID provided.");
            throw new InvalidCustomerException("Invalid customer ID provided.");
        }
        if (!accountCreationValidation.isValidCountry(account.getCountry())) {
            log.info("Invalid country provided.");
            throw new InvalidCountryException("Invalid country provided.");
        }

        accountRepository.insertAccount(account);

        List<Balance> balances = new ArrayList<>();

        // Create a balance with zero amount for each currency provided
        // private List<Balance> balances in Account.java
        for (Balance currency : account.getBalances()) {
            log.info("Creating balance for currency: {}", currency);
            Balance balance = new Balance();
            balance.setAccountId(account.getAccountId());
            balance.setAvailableAmount(BigDecimal.ZERO);
            balance.setCurrency(String.valueOf(currency));
            balanceRepository.insertBalance(balance);
            balances.add(balance);
        }

        // Set the balances to the account
        account.setBalances(balances);

        // Publish the account creation event
        messagePublisher.publishAccountEvent(account);
        log.info("Account created: {}", account);
        return account;
    }
}

