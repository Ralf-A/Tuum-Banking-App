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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private MessagePublisher rabbitMQPublisher;

    @Autowired
    private AccountCreationValidation accountCreationValidation;

    public Account getAccountById(Long accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account not found with id: " + accountId);
        }
        account.setBalances(balanceRepository.findBalancesByAccountId(accountId));
        return account;
    }

    public Account createAccount(Account account) throws InvalidCurrencyException, InvalidCustomerException {
        if (!accountCreationValidation.isValidCurrency(account.getBalances().stream().map(Balance::getCurrency).collect(Collectors.toList()))) {
            throw new InvalidCurrencyException("Invalid currency provided.");
        }
        if (!accountCreationValidation.isValidCustomerId(account.getCustomerId())) {
            throw new InvalidCustomerException("Invalid customer ID provided.");
        }
        if (!accountCreationValidation.isValidCountry(account.getCountry())) {
            throw new InvalidCountryException("Invalid country provided.");
        }

        accountRepository.insertAccount(account);
        for (Balance balance : account.getBalances()) {
            balance.setAccountId(account.getAccountId());
            balanceRepository.insertBalance(balance);
        }
        rabbitMQPublisher.publishAccountEvent(account);

        return account;
    }
}

