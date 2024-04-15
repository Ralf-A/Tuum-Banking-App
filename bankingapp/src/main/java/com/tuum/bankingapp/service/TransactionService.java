package com.tuum.bankingapp.service;

import com.tuum.bankingapp.exception.*;
import com.tuum.bankingapp.messaging.MessagePublisher;
import com.tuum.bankingapp.model.Transaction;
import com.tuum.bankingapp.repository.BalanceRepository;
import com.tuum.bankingapp.repository.TransactionRepository;
import com.tuum.bankingapp.validation.TransactionValidation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for handling transaction related operations
 */
@Service
public class TransactionService {
    Logger log = org.slf4j.LoggerFactory.getLogger(TransactionService.class);

    // Constructor and autowiring the dependencies
    private final MessagePublisher messagePublisher;
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;
    private final TransactionValidation transactionValidation;
    @Autowired
    public TransactionService(MessagePublisher messagePublisher, TransactionRepository transactionRepository,
                               BalanceRepository balanceRepository, TransactionValidation transactionValidation) {
        this.messagePublisher = messagePublisher;
        this.transactionRepository = transactionRepository;
        this.balanceRepository = balanceRepository;
        this.transactionValidation = transactionValidation;
    }

    /**
     * Creates a transaction for the given account
     * @param accountId Account ID
     * @param amount Transaction amount
     * @param currency Currency
     * @param direction Transaction direction (IN/OUT)
     * @param description Transaction description
     * @return Created transaction
     */
    public Transaction createTransaction(Long accountId, BigDecimal amount, String currency,
                                         String direction, String description) {
        log.info("Creating transaction for account: {}, amount: {}, currency: {}, direction: {}, description: {}",
                accountId, amount, currency, direction, description);

        // Validate input parameters
        validateCurrency(currency);
        validateDirection(direction);
        validateDescription(description);
        validateAmount(amount);

        // Retrieve and validate the current balance
        BigDecimal currentBalance = balanceRepository.findAvailableAmountByAccountIdAndCurrency(accountId, currency);
        validateCurrentBalance(currentBalance, accountId);

        // Calculate the new balance and validate sufficient funds
        BigDecimal newBalance = calculateNewBalance(currentBalance, amount, direction);
        validateSufficientFunds(newBalance, direction);

        // Update balance in the database
        balanceRepository.updateAvailableAmountByAccountIdAndCurrency(newBalance, accountId, currency);

        // Create and save the transaction
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setDirection(direction);
        transaction.setDescription(description);
        transaction.setBalanceAfterTransaction(newBalance);

        // Save the transaction and publish the event
        Long transactionId = transactionRepository.createTransaction(transaction);
        transaction.setTransactionId(transactionId);
        log.info("Transaction created: {}", transaction);
        messagePublisher.publishTransactionEvent(transaction);

        return transaction;
    }

    /**
     * Retrieves transactions for the given account
     * @param accountId Account ID
     * @return List of transactions
     */
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        log.info("Retrieving transactions for account: {}", accountId);
        validateAccountId(accountId);
        List<Transaction> transactions = transactionRepository.findTransactionsByAccountId(accountId);
        log.info("Transactions retrieved for account: {}", accountId);
        return transactions;
    }

    /**
     * Validates the currency
     * @param currency Currency
     */
    private void validateCurrency(String currency) {
        if (!transactionValidation.isCurrencyValid(currency)) {
            log.error("Invalid currency: {}", currency);
            throw new InvalidCurrencyException("Invalid currency: " + currency);
        }
    }

    /** Validates the amount
     * @param amount Transaction amount
     */
    private void validateAmount(BigDecimal amount) {
        if (!transactionValidation.isAmountValid(amount.doubleValue())) {
            log.error("Invalid amount: {}", amount);
            throw new InvalidAmountException("Invalid amount: " + amount);
        }
    }

    /**
     * Validates the direction
     * @param direction Transaction direction (IN/OUT)
     */
    private void validateDirection(String direction) {
        if (!transactionValidation.isTransactionTypeValid(direction)) {
            log.error("Invalid direction: {}", direction);
            throw new InvalidDirectionException("Invalid direction: " + direction);
        }
    }

    /**
     * Validates the description
     * @param description Transaction description
     */
    private void validateDescription(String description) {
        if (!transactionValidation.isDescriptionValid(description)) {
            log.error("Invalid description: {}", description);
            throw new InvalidDescriptionException("Description cannot be empty.");
        }
    }

    /** Validates the current balance
     * @param currentBalance Current balance
     * @param accountId Account ID
     */
    private void validateCurrentBalance(BigDecimal currentBalance, Long accountId) {
        if (currentBalance == null) {
            log.error("Account balance missing for account ID: {}", accountId);
            throw new AccountNotFoundException("Account balance missing for account ID: " + accountId);
        }
    }

    /**
     * Calculates the new balance
     * @param currentBalance Current balance
     * @param amount Transaction amount
     * @param direction Transaction direction (IN/OUT)
     * @return New balance
     */
    private BigDecimal calculateNewBalance(BigDecimal currentBalance, BigDecimal amount, String direction) {
        log.info("Calculating new balance for current balance: {}, amount: {}, direction: {}", currentBalance, amount, direction);
        return "IN".equals(direction) ? currentBalance.add(amount) : currentBalance.subtract(amount);
    }

    /**
     * Validates the sufficient funds
     * @param newBalance New balance
     * @param direction Transaction direction (IN/OUT)
     */
    private void validateSufficientFunds(BigDecimal newBalance, String direction) {
        if ("OUT".equals(direction) && newBalance.compareTo(BigDecimal.ZERO) < 0) {
            log.error("Insufficient funds for transaction.");
            throw new InsufficientFundsException("Insufficient funds for transaction.");
        }
    }

    /**
     * Validates the account ID
     * @param accountId Account ID
     */
    private void validateAccountId(Long accountId) {
        if (!transactionValidation.isAccountIdValid(accountId)) {
            log.error("Invalid account ID: {}", accountId);
            throw new InvalidAccountException("Invalid account ID: " + accountId);
        }
    }
}







