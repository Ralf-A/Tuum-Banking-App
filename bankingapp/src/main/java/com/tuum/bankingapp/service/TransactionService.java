package com.tuum.bankingapp.service;

import com.tuum.bankingapp.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    public Transaction createTransaction(String accountId, String amount, String currency, String direction, String description) {
        return new Transaction();
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return new ArrayList<>();
    }
}






