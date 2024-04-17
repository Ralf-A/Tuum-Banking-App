package com.tuum.bankingapp.service;

import com.tuum.bankingapp.exception.AccountNotFoundException;
import com.tuum.bankingapp.messaging.MessagePublisher;
import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.model.Balance;
import com.tuum.bankingapp.repository.AccountBalanceRepository;
import com.tuum.bankingapp.repository.AccountRepository;
import com.tuum.bankingapp.repository.BalanceRepository;
import com.tuum.bankingapp.validation.AccountValidation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Service class for handling account related operations
 */
@Service
public class AccountService {
    Logger log = org.slf4j.LoggerFactory.getLogger(AccountService.class);

    // Constructor and autowiring the dependencies
    private final MessagePublisher messagePublisher;
    private final BalanceRepository balanceRepository;
    private final AccountValidation accountValidation;
    private final AccountRepository accountRepository;
    private final AccountBalanceRepository accountBalanceRepository;
    @Autowired
    public AccountService(MessagePublisher messagePublisher, BalanceRepository balanceRepository,
                          AccountValidation accountValidation, AccountRepository accountRepository,
                          AccountBalanceRepository accountBalanceRepository) {
        this.messagePublisher = messagePublisher;
        this.balanceRepository = balanceRepository;
        this.accountValidation = accountValidation;
        this.accountRepository = accountRepository;
        this.accountBalanceRepository = accountBalanceRepository;
    }

    /**
     * Fetches account by account ID
     * @param accountId Account ID
     * @return Account object
     */
    public Account getAccountById(Long accountId) {
        log.info("Finding account by ID: {}", accountId);
        Account account = accountRepository.findAccountById(accountId);
        if (account == null || accountId == null || accountId < 0) {
            log.error("Account not found for ID: {}", accountId);
            throw new AccountNotFoundException("Account not found");
        }
        // Fetch balances for the account
        List<Balance> balances = getBalancesForAccount(accountId);
        account.setBalances(balances);
        log.info("Account found: {}", account);
        return account;
    }

    /**
     * Fetches balances for account by account ID
     * @param accountId Account ID
     * @return List of balances
     */
    private List<Balance> getBalancesForAccount(Long accountId) {
        log.info("Finding balances for account: {}", accountId);
        if (accountId == null || accountId < 0) {
            log.error("Invalid account ID: {}", accountId);
            throw new AccountNotFoundException("Invalid account ID");
        }
        List<Long> balanceIds = accountBalanceRepository.findBalanceIdsByAccountId(accountId);
        log.info("Found balance IDs: {}", balanceIds);
        return balanceIds.stream()
                .map(balanceRepository::findBalanceById)
                .collect(Collectors.toList());
    }

    /**
     * Creates an account for a customer
     * @param customerId Customer ID
     * @param country Country
     * @param currencies List of currencies
     * @return Created account object
     */
    @Transactional
    public Account createAccount(Long customerId, String country, List<String> currencies) {
        accountValidation.validateAccount(customerId, country, currencies);
        Account account = new Account();
        account.setCustomerId(customerId);
        account.setCountry(country);
        account.setBalances(new ArrayList<>());

        log.info("Creating account for customer ID: {}", customerId);

        accountRepository.insertAccount(account);
        Long accountId = account.getAccountId();
        // Create balances for the account, initializes them with zero balance
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
}


