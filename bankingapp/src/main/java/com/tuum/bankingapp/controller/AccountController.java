package com.tuum.bankingapp.controller;

import com.tuum.bankingapp.dto.CreateAccountRequest;
import com.tuum.bankingapp.exception.AccountNotFoundException;
import com.tuum.bankingapp.exception.InvalidCountryException;
import com.tuum.bankingapp.exception.InvalidCurrencyException;
import com.tuum.bankingapp.exception.InvalidCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tuum.bankingapp.service.AccountService;
import com.tuum.bankingapp.model.Account;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request) {
        try {
            Account createdAccount = accountService.createAccount(request.getCustomerId(), request.getCountry(), request.getCurrency());
            return ResponseEntity.ok(createdAccount);
        } catch (InvalidCountryException | InvalidCustomerException | InvalidCurrencyException e) {
            // Handle the case where an invalid argument is provided
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable Long accountId) {
        try {
            Account account = accountService.getAccountById(accountId);
            return ResponseEntity.ok(account);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

