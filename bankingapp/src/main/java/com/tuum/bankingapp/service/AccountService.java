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
        account.setBalances(getBalancesForAccount(accountId));
        log.info("Account found: {}", account);
        return account;
    }

    private Map<String, Balance> getBalancesForAccount(Long accountId) {
        return (Map<String, Balance>) balanceRepository.findBalancesByAccountId(accountId);
    }

    public Account createAccount(Account account) {
        log.info("Creating account: {}", account);
        validateAccount(account);

        accountRepository.insertAccount(account);

        // Insert balances
        List<Balance> balances = new ArrayList<>(account.getBalances().values());
        for (Balance balance : balances) {
            balance.setAccountId(account.getAccountId());
            balanceRepository.insertBalance(balance);
        }

        log.info("Account created: {}", account);
        return account;
    }

    private void validateAccount(Account account) {
        // Validate currencies
//        if (!accountCreationValidation.isValidCurrency(currencies)) {
//            log.info("Invalid currency provided.");
//            throw new InvalidCurrencyException("Invalid currency provided.");
//        }

        // Validate customer ID
        if (!accountCreationValidation.isValidCustomerId(account.getCustomerId())) {
            log.info("Invalid customer ID provided.");
            throw new InvalidCustomerException("Invalid customer ID provided.");
        }

        // Validate country
        if (!accountCreationValidation.isValidCountry(account.getCountry())) {
            log.info("Invalid country provided.");
            throw new InvalidCountryException("Invalid country provided.");
        }
    }

}

