package com.tuum.bankingapp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tuum.bankingapp.service.TransactionService;
import com.tuum.bankingapp.model.Transaction;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        // Logic to create a transaction
        return ResponseEntity.ok(transactionService.createTransaction(transaction));
    }

    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long accountId) {
        // Logic to get transactions for an account
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }
}
