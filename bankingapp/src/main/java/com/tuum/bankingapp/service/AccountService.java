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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Account createAccount(Account account) {
        // Validate account
        log.info("Creating account, validating account details for account");
        validateAccount(account);

        // Insert account into the database
        accountRepository.insertAccount(account);
        Long accountId = account.getAccountId(); // Ensure this is being retrieved after insertion

        // Insert balances and link them to the account using the account_balances middle-table
        for (Balance balance : account.getBalances()) {
            // Insert balance into the database
            balanceRepository.insertBalance(balance);
            Long balanceId = balance.getBalanceId(); // Ensure this is retrieved after insertion

            // Insert record into the account_balances middle-table
            accountBalanceRepository.insertAccountBalance(accountId, balanceId);
        }
        // Publish account creation event
        messagePublisher.publishAccountEvent(account);
        log.info("Account created successfully: {}", accountId);
        return account;
    }


    private void validateAccount(Account account) {
        // Validate country
        if (!accountCreationValidation.isValidCountry(account.getCountry())) {
            throw new IllegalArgumentException("Invalid country");
        }
        // Validate customer ID
        if (!accountCreationValidation.isValidCustomerId(account.getCustomerId())) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        // Validate currencies
        List<String> currencies = account.getBalances().stream()
                .map(Balance::getCurrency)
                .collect(Collectors.toList());
        if (!accountCreationValidation.isValidCurrency(currencies)) {
            throw new IllegalArgumentException("Invalid currency");
        }
    }

}

