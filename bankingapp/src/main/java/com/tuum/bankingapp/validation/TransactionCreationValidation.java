package com.tuum.bankingapp.validation;

import com.tuum.bankingapp.exception.*;
import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.repository.AccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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

    // Validation transaction details from DTO input
    @Override
    public void validateTransaction(Long accountId, Double amount, String currency, String transactionType, String description) {
        log.info("Validating transaction for account: {}, amount: {}, currency: {}, direction: {}, description: {}",
                accountId, amount, currency, transactionType, description);
        isAccountIdValid(accountId);
        isAmountValid(amount);
        isCurrencyValid(currency);
        isTransactionTypeValid(transactionType);
        isDescriptionValid(description);
        log.info("Transaction details are valid.");
    }

    /** Validates the current balance
     * @param currentBalance Current balance
     * @param accountId Account ID
     */
    public void isValidCurrentBalance(BigDecimal currentBalance, Long accountId, String currency) {
        if (currentBalance == null) {
            log.error("Account doesn't have this currency, {}, for account ID: {}", currency, accountId);
            throw new AccountNotFoundException("Account doesn't have this currency, " + currency + ", for account ID: " + accountId);
        }
    }

    /**
     * Validates the sufficient funds
     * @param newBalance New balance
     * @param direction Transaction direction (IN/OUT)
     */
    public void isValidSufficientFunds(BigDecimal newBalance, String direction) {
        if ("OUT".equals(direction) && newBalance.compareTo(BigDecimal.ZERO) < 0) {
            log.error("Insufficient funds for transaction.");
            throw new InsufficientFundsException("Insufficient funds for transaction.");
        }
    }

    /**
     * Validates the account ID
     * @param accountId Account ID
     */
    public void isAccountIdValid(Long accountId) {
        if (accountId == null || accountId <= 0) {
            log.error("Account ID is null or invalid: {}", accountId);
            throw new InvalidAccountException("Account ID is null or invalid: " + accountId);
        }
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            log.error("Account ID does not exist: {}", accountId);
            throw new InvalidAccountException("Account with such ID does not exist: " + accountId);
        }
    }

    /**
     * Validates the amount
     * @param amount Transaction amount
     */
    public void isAmountValid(Double amount) {
        if (amount == null || amount <= 0) {
            log.error("Amount is null or invalid: {}", amount);
            throw new InvalidAmountException("Amount is null or invalid: " + amount);
        }
    }

    /**
     * Validates the currency
     * @param currency Currency
     */
    public void isCurrencyValid(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            log.error("Currency is null or empty: {}", currency);
            throw new InvalidCurrencyException("Currency is null or empty: " + currency);
        }
    }

    /**
     * Validates the transaction type
     * @param transactionType Transaction type
     */
    public void isTransactionTypeValid(String transactionType) {
        if (transactionType == null || transactionType.trim().isEmpty()) {
            log.error("Transaction type is null or empty: {}", transactionType);
            throw new InvalidDirectionException("Invalid direction: " + transactionType);
        }
        if (!transactionType.equals("OUT") && !transactionType.equals("IN")) {
            log.error("Invalid transaction type: {}", transactionType);
            throw new InvalidDirectionException("Invalid transaction type: " + transactionType);
        }
    }

    /**
     * Validates the description
     * @param description Transaction description
     */
    public void isDescriptionValid(String description) {
        if (description == null || description.trim().isEmpty()) {
            log.error("Description is null or empty: {}", description);
            throw new InvalidDescriptionException("Description is null or empty.");
        }
    }
}
