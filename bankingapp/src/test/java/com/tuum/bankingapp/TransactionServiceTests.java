package com.tuum.bankingapp;

import com.tuum.bankingapp.messaging.MessagePublisher;
import com.tuum.bankingapp.model.Transaction;
import com.tuum.bankingapp.repository.TransactionRepository;
import com.tuum.bankingapp.service.TransactionService;
import com.tuum.bankingapp.repository.BalanceRepository;
import com.tuum.bankingapp.validation.TransactionValidation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;


import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionValidation transactionValidation;
    @Mock
    private MessagePublisher messagePublisher;
    @Mock
    private BalanceRepository balanceRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        transactionService = new TransactionService(messagePublisher, transactionRepository, balanceRepository, transactionValidation);
    }

    @Test
    public void createTransactionTest() {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal(100);
        String currency = "USD";
        String direction = "IN";
        String description = "Test transaction";

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setTransactionId(0L);
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setDirection(direction);
        transaction.setBalanceAfterTransaction(new BigDecimal(1100));
        transaction.setDescription(description);

        when(balanceRepository.findAvailableAmountByAccountIdAndCurrency(accountId, currency)).thenReturn(new BigDecimal(1000));
        transactionValidation.isValidCurrentBalance(new BigDecimal(1000), accountId, currency);
        transactionValidation.validateTransaction(accountId, amount.doubleValue(), currency, direction, description);

        Transaction createdTransaction = transactionService.createTransaction(accountId, amount, currency, direction, description);

        Assertions.assertEquals(transaction, createdTransaction);
    }

    @Test
    public void getTransactionsByAccountIdTest() {
        Long accountId = 1L;
        List<Transaction> transactions = List.of(new Transaction(), new Transaction(), new Transaction());
        when(transactionService.getTransactionsByAccountId(accountId)).thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactionsByAccountId(accountId);

        Assertions.assertEquals(transactions, result);
    }

}
