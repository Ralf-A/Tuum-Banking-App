package com.tuum.bankingapp.service;

import com.tuum.bankingapp.exception.AccountNotFoundException;
import com.tuum.bankingapp.exception.GlobalExceptionHandler;
import com.tuum.bankingapp.exception.InsufficientFundsException;
import com.tuum.bankingapp.exception.InvalidAccountException;
import com.tuum.bankingapp.model.Balance;
import com.tuum.bankingapp.model.Transaction;
import com.tuum.bankingapp.repository.AccountRepository;
import com.tuum.bankingapp.repository.BalanceRepository;
import com.tuum.bankingapp.repository.TransactionRepository;
import com.tuum.bankingapp.validation.TransactionValidation;
import com.tuum.bankingapp.exception.InvalidTransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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

    public Transaction createTransaction(String accountIdStr, String amountStr, String currency, String direction, String description) throws InvalidTransactionException {
        log.info("Creating transaction for account: {}, amount: {}, currency: {}, direction: {}, description: {}",
                accountIdStr, amountStr, currency, direction, description);
        Long accountId = Long.parseLong(accountIdStr);
        BigDecimal amount = new BigDecimal(amountStr);

        // Validate input parameters
        if (!transactionValidation.isAccountIdValid(accountId) ||
                !transactionValidation.isAmountValid(amount.doubleValue()) ||
                !transactionValidation.isCurrencyValid(currency) ||
                !transactionValidation.isTransactionTypeValid(direction) ||
                !transactionValidation.isDescripitonValid(description)) {
            log.error("Invalid transaction parameters");
            throw new InvalidTransactionException("Invalid transaction parameters");
        }

        // Retrieve the current balance
        BigDecimal currentBalance = balanceRepository.findAvailableAmountByAccountIdAndCurrency(accountId, currency);
        if (currentBalance == null) {
            log.error("Account balance missing");
            throw new AccountNotFoundException("Account balance missing");
        }

        // Calculate the new balance based on the transaction direction
        BigDecimal newBalance = "IN".equals(direction) ? currentBalance.add(amount) : currentBalance.subtract(amount);

        // Check for sufficient funds in case of OUT direction
        if ("OUT".equals(direction) && newBalance.compareTo(BigDecimal.ZERO) < 0) {
            log.error("Insufficient funds for transaction");
            throw new InsufficientFundsException("Insufficient funds for transaction");
        }

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
        return transaction;
    }


    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        log.info("Retrieving transactions for account: {}", accountId);
        if (!transactionValidation.isAccountIdValid(accountId)) {
            log.error("Invalid account ID");
            throw new InvalidAccountException("Invalid account ID");
        }
        log.info("Transactions retrieved for account: {}", accountId);
        return transactionRepository.findTransactionsByAccountId(accountId);
    }
}







