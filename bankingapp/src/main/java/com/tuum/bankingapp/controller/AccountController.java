package com.tuum.bankingapp.controller;

import com.tuum.bankingapp.dto.AccountDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tuum.bankingapp.service.AccountService;
import com.tuum.bankingapp.model.Account;


/**
 * Responsible for handling the incoming HTTP requests related to the account
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * POST request to create a new account
     * @param request the request body as an AccountDTO object
     * @return the created account
     */
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDTO request) {
        Account createdAccount = accountService.createAccount(request.getCustomerId(), request.getCountry(), request.getCurrency());
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    /**
     * GET request to retrieve an account by its ID
     * @param accountId the ID of the account to retrieve
     * @return the account with the given ID
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
}


