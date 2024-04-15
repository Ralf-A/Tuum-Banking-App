package com.tuum.bankingapp.validation;

import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.repository.AccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class for validating transaction related input parameters
 */
@Component
public class TransactionCreationValidation implements TransactionValidation{
    Logger log = org.slf4j.LoggerFactory.getLogger(TransactionCreationValidation.class);

    // Constructor and autowiring the dependencies
    private final AccountRepository accountRepository;
    @Autowired
    public TransactionCreationValidation(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Validates the account ID
     * @param accountId Account ID
     * @return True if the account ID is valid, meaning not null and existing, false otherwise
     */
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

    /**
     * Validates the amount
     * @param amount Transaction amount
     * @return True if the amount is valid, meaning not null and greater than 0, false otherwise
     */
    public boolean isAmountValid(Double amount) {
        if (amount == null || amount <= 0) {
            log.error("Amount is null or invalid: {}", amount);
            return false;
        }
        return true;
    }

    /**
     * Validates the currency
     * @param currency Currency
     * @return True if the currency is valid, meaning not null and not empty, false otherwise
     */
    public boolean isCurrencyValid(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            log.error("Currency is null or empty: {}", currency);
            return false;
        }
        return true;
    }

    /**
     * Validates the transaction type
     * @param transactionType Transaction type
     * @return True if the transaction type is valid, meaning not null and either "IN" or "OUT", false otherwise
     */
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

    /**
     * Validates the description
     * @param description Transaction description
     * @return True if the description is valid, meaning not null and not empty, false otherwise
     */
    public boolean isDescriptionValid(String description) {
        if (description == null || description.trim().isEmpty()) {
            log.error("Description is null or empty: {}", description);
            return false;
        }
        return true;
    }
}
