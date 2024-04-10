package com.tuum.bankingapp.validation;

import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.repository.AccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TransactionValidation {
    Logger log = org.slf4j.LoggerFactory.getLogger(TransactionValidation.class);
    @Autowired
    private AccountRepository accountRepository;

    public boolean isAccountIdValid(Long accountId) {
        if (accountId == null || accountId <= 0) {
            log.error("Account ID is null or invalid: {}", accountId);
            return false;
        }
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            log.error("Account ID does not exist: {}", accountId);
            return false;
        }
        return true;
    }

    public boolean isAmountValid(Double amount) {
        if (amount == null || amount <= 0) {
            log.error("Amount is null or invalid: {}", amount);
            return false;
        }
        return true;
    }

    public boolean isCurrencyValid(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            log.error("Currency is null or empty: {}", currency);
            return false;
        }
        return true;
    }

    public boolean isTransactionTypeValid(String transactionType) {
        if (transactionType == null || transactionType.trim().isEmpty()) {
            log.error("Transaction type is null or empty: {}", transactionType);
            return false;
        }
        if (!transactionType.equals("OUT") && !transactionType.equals("IN")) {
            log.error("Invalid transaction type: {}", transactionType);
            return false;
        }
        return true;
    }
    public boolean isDescripitonValid(String description) {
        if (description == null || description.trim().isEmpty()) {
            log.error("Description is null or empty: {}", description);
            return false;
        }
        return true;
    }
}
