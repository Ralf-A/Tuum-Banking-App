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

/**
 * Controller class for handling transaction-related requests.
 */
@RestController
@RequestMapping("/api/accounts/{accountId}/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Create a new transaction.
     *
     * @param accountId the ID of the account to create the transaction for
     * @param request   the request body as a TransactionDTO object
     * @return the created transaction
     */
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@PathVariable Long accountId,
                                                            @Valid @RequestBody TransactionDTO request) {
        Transaction createdTransaction = transactionService.createTransaction(accountId, request.getAmount(),
                request.getCurrency(), request.getDirection(), request.getDescription());
        TransactionDTO transactionDTO = convertToDTO(createdTransaction);
        return new ResponseEntity<>(transactionDTO, HttpStatus.CREATED);
    }

    /**
     * Get all transactions for an account.
     * @param accountId the ID of the account to get transactions for
     * @return a list of transactions
     */
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getTransactions(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transactionDTOs);
    }

    /**
     * Convert a Transaction object to a TransactionDTO object.
     * @param transaction the Transaction object to convert
     * @return the converted TransactionDTO object
     */
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
