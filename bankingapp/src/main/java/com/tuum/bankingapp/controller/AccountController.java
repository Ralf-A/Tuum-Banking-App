package com.tuum.bankingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tuum.bankingapp.service.AccountService;
import com.tuum.bankingapp.model.Account;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        try {
            Account createdAccount = accountService.createAccount(account);
            return ResponseEntity.ok(createdAccount);
        } catch (IllegalArgumentException e) {
            // Handle the case where an invalid currency is provided
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable Long accountId) {
        try {
            Account account = accountService.getAccountById(accountId);
            if (account != null) {
                return ResponseEntity.ok(account);
            } else {
                // Handle the case where the account is not found
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle any other exceptions
            return ResponseEntity.internalServerError().build();
        }
    }
}

