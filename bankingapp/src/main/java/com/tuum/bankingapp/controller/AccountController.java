package com.tuum.bankingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tuum.bankingapp.service.AccountService;
import com.tuum.bankingapp.model.Account;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        // Logic to create an account
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable Long accountId) {
        // Logic to get an account
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }
}

