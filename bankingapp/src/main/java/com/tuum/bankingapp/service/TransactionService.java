package com.tuum.bankingapp.service;

import com.tuum.bankingapp.exception.InvalidTransactionException;
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

        // Retrieve and validate current balance
        BigDecimal currentBalance = balanceRepository.findAvailableAmountByAccountIdAndCurrency(accountId, currency);
        transactionValidation.isValidCurrentBalance(currentBalance, accountId, currency);

        // Validate the transaction parameters
        transactionValidation.validateTransaction(accountId, amount.doubleValue(), currency, direction, description);

        // Calculate new balance after transaction
        BigDecimal newBalance = calculateNewBalance(currentBalance, amount, direction);

        // Validate sufficient funds
        transactionValidation.isValidSufficientFunds(newBalance, direction);

        try {
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
        } catch (Exception e) {
            // Rollback transaction in case of error
            log.error("Error creating transaction: {}", e.getMessage());
            throw new InvalidTransactionException("Error creating transaction" + e.getMessage());
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
     * Retrieves transactions for the given account
     * @param accountId Account ID
     * @return List of transactions
     */
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        log.info("Retrieving transactions for account: {}", accountId);
        transactionValidation.isAccountIdValid(accountId);
        List<Transaction> transactions = transactionRepository.findTransactionsByAccountId(accountId);
        log.info("Transactions retrieved for account: {}", accountId);
        return transactions;
    }
}







