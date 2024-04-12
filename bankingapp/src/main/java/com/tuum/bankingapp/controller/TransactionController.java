package com.tuum.bankingapp.controller;
import com.tuum.bankingapp.dto.TransactionDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tuum.bankingapp.service.TransactionService;
import com.tuum.bankingapp.model.Transaction;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts/{accountId}/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@PathVariable Long accountId,
                                                            @Valid @RequestBody TransactionDTO request) {
        Transaction createdTransaction = transactionService.createTransaction(accountId, request.getAmount(),
                request.getCurrency(), request.getDirection(), request.getDescription());
        TransactionDTO transactionDTO = convertToDTO(createdTransaction);
        return new ResponseEntity<>(transactionDTO, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getTransactions(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transactionDTOs);
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setAccountId(transaction.getAccountId());
        dto.setAmount(transaction.getAmount());
        dto.setCurrency(transaction.getCurrency());
        dto.setDirection(transaction.getDirection());
        dto.setDescription(transaction.getDescription());
        return dto;
    }
}
