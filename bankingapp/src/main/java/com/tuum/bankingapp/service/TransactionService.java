package com.tuum.bankingapp.service;

import com.tuum.bankingapp.exception.*;
import com.tuum.bankingapp.messaging.MessagePublisher;
import com.tuum.bankingapp.model.Transaction;
import com.tuum.bankingapp.repository.AccountRepository;
import com.tuum.bankingapp.repository.BalanceRepository;
import com.tuum.bankingapp.repository.TransactionRepository;
import com.tuum.bankingapp.validation.TransactionValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private TransactionValidation transactionValidation;

    @Autowired
    private MessagePublisher messagePublisher;

    public Transaction createTransaction(String accountIdStr, String amountStr, String currency, String direction, String description) {
        log.info("Creating transaction for account: {}, amount: {}, currency: {}, direction: {}, description: {}",
                accountIdStr, amountStr, currency, direction, description);

        // Convert and validate input parameters
        Long accountId = convertToLong(accountIdStr);
        BigDecimal amount = convertToBigDecimal(amountStr, "Amount");

        validateCurrency(currency);
        validateDirection(direction);
        validateDescription(description);
        validateAmount(amount);
        BigDecimal currentBalance = balanceRepository.findAvailableAmountByAccountIdAndCurrency(accountId, currency);
        validateCurrentBalance(currentBalance, accountId);
        log.info("Validated input parameters for account: {}, amount: {}, currency: {}, direction: {}, description: {}",
                accountId, amount, currency, direction, description);

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

        Long transactionId = transactionRepository.createTransaction(transaction);
        transaction.setTransactionId(transactionId);

        log.info("Transaction created: {}", transaction);
        messagePublisher.publishTransactionEvent(transaction);
        return transaction;
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        log.info("Retrieving transactions for account: {}", accountId);
        validateAccountId(accountId);
        List<Transaction> transactions = transactionRepository.findTransactionsByAccountId(accountId);
        log.info("Transactions retrieved for account: {}", accountId);
        return transactions;
    }

    // Helper methods for validation and conversion
    private Long convertToLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.error("Account ID must be a valid number.");
            throw new InvalidParameterException("Account ID" + " must be a valid number.");
        }
    }


    private BigDecimal convertToBigDecimal(String value, String fieldName) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(fieldName + " must be a valid amount.");
        }
    }

    private void validateCurrency(String currency) {
        if (!transactionValidation.isCurrencyValid(currency)) {
            log.error("Invalid currency: {}", currency);
            throw new InvalidCurrencyException("Invalid currency: " + currency);
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (!transactionValidation.isAmountValid(amount.doubleValue())) {
            log.error("Invalid amount: {}", amount);
            throw new InvalidAmountException("Invalid amount: " + amount);
        }
    }

    private void validateDirection(String direction) {
        if (!transactionValidation.isTransactionTypeValid(direction)) {
            log.error("Invalid direction: {}", direction);
            throw new InvalidDirectionException("Invalid direction: " + direction);
        }
    }

    private void validateDescription(String description) {
        if (!transactionValidation.isDescripitonValid(description)) {
            log.error("Invalid description: {}", description);
            throw new InvalidDescriptionException("Description cannot be empty.");
        }
    }

    private void validateCurrentBalance(BigDecimal currentBalance, Long accountId) {
        if (currentBalance == null) {
            log.error("Account balance missing for account ID: {}", accountId);
            throw new AccountNotFoundException("Account balance missing for account ID: " + accountId);
        }
    }

    private BigDecimal calculateNewBalance(BigDecimal currentBalance, BigDecimal amount, String direction) {
        log.info("Calculating new balance for current balance: {}, amount: {}, direction: {}", currentBalance, amount, direction);
        return "IN".equals(direction) ? currentBalance.add(amount) : currentBalance.subtract(amount);
    }

    private void validateSufficientFunds(BigDecimal newBalance, String direction) {
        if ("OUT".equals(direction) && newBalance.compareTo(BigDecimal.ZERO) < 0) {
            log.error("Insufficient funds for transaction.");
            throw new InsufficientFundsException("Insufficient funds for transaction.");
        }
    }

    private void validateAccountId(Long accountId) {
        if (!transactionValidation.isAccountIdValid(accountId)) {
            log.error("Invalid account ID: {}", accountId);
            throw new InvalidAccountException("Invalid account ID: " + accountId);
        }
    }
}







