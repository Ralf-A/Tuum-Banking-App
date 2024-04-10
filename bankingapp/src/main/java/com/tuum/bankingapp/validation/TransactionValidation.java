package com.tuum.bankingapp.validation;

import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.repository.AccountRepository;
import com.tuum.bankingapp.repository.TransactionRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionValidation {
    Logger log = org.slf4j.LoggerFactory.getLogger(TransactionValidation.class);
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    public boolean isAccountIdValid(Long accountId) {
        if (accountId == null || accountId <= 0) {
            log.info("Account ID is null or invalid: {}", accountId);
            return false;
        }
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            log.info("Account ID does not exist: {}", accountId);
            return false;
        }
        return true;
    }

    public boolean isAmountValid(Double amount) {
        if (amount == null || amount <= 0) {
            log.info("Amount is null or invalid: {}", amount);
            return false;
        }
        return true;
    }

    public boolean isCurrencyValid(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            log.info("Currency is null or empty: {}", currency);
            return false;
        }
        return true;
    }

    public boolean isTransactionTypeValid(String transactionType) {
        if (transactionType == null || transactionType.trim().isEmpty()) {
            log.info("Transaction type is null or empty: {}", transactionType);
            return false;
        }
        if (!transactionType.equals("OUT") && !transactionType.equals("IN")) {
            log.info("Invalid transaction type: {}", transactionType);
            return false;
        }
        return true;
    }
    public boolean isDescripitonValid(String description) {
        if (description == null || description.trim().isEmpty()) {
            log.info("Description is null or empty: {}", description);
            return false;
        }
        return true;
    }
}
