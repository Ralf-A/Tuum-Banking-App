package com.tuum.bankingapp.controller;
import com.tuum.bankingapp.dto.CreateTransactionRequest;
import com.tuum.bankingapp.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tuum.bankingapp.service.TransactionService;
import com.tuum.bankingapp.model.Transaction;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@RequestBody CreateTransactionRequest request) {
        try{
            Transaction createdTransaction = transactionService.createTransaction(request.getAccountId(), request.getAmount(), request.getCurrency(), request.getDirection(), request.getDescription());
            return ResponseEntity.ok(createdTransaction);
        } catch (AccountNotFoundException | InvalidCurrencyException | InvalidAmountException | InvalidDescriptionException | InvalidDirectionException | InvalidTransactionException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An unexpected error occurred");
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getTransactions(@PathVariable Long accountId) {
        try{
            List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
            return ResponseEntity.ok(transactions);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
