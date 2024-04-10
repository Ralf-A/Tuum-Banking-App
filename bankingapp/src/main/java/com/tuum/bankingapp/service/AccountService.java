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
        // Find account by account ID
        log.info("Finding account by ID: {}", accountId);
        if (accountId == null || accountRepository.findAccountById(accountId) == null){
            log.info("Account not found");
            throw new AccountNotFoundException("Account not found");
        }
        log.info("Account found: {}", accountId);
        return accountRepository.findAccountById(accountId);
    }

    private List<Balance> getBalancesForAccount(Long accountId) {
        // Find balance IDs by account ID from the account_balances join table
        log.info("Finding balances for account: {}", accountId);
        try {
            List<Long> balanceIds = accountBalanceRepository.findBalanceIdsByAccountId(accountId);
            log.info("Found balance IDs: {}", balanceIds);
            return balanceIds.stream()
                    .map(balanceRepository::findBalanceById)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error occurred while fetching balances for account: {}", accountId);
            throw new AccountNotFoundException("Error occurred while fetching balances for account");
        }
    }

    @Transactional
    public Account createAccount(Account account) {
        // Validate account, insert account and balances, and publish account creation event
        validateAccount(account);
        accountRepository.insertAccount(account);
        for (Balance balance : account.getBalances()) {
            balanceRepository.insertBalance(balance);
            accountBalanceRepository.insertAccountBalance(account.getAccountId(), balance.getBalanceId());
        }
        messagePublisher.publishAccountEvent(account);
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

