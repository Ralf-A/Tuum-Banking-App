package com.tuum.bankingapp;
import com.tuum.bankingapp.exception.AccountNotFoundException;
import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.model.Balance;
import com.tuum.bankingapp.repository.AccountBalanceRepository;
import com.tuum.bankingapp.repository.AccountRepository;
import com.tuum.bankingapp.repository.BalanceRepository;
import com.tuum.bankingapp.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;


@SpringBootTest
public class AccountServiceIntegrationTest {
    @Autowired
    private AccountService accountService;
    @MockBean
    private BalanceRepository balanceRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private AccountBalanceRepository accountBalanceRepository;

    Long accountId_valid = 123L;
    Long accountId_Invalid = -1L;

    Long customerId_valid = 123L;

    String country_valid = "EST";

    List<String> currencies_valid = Arrays.asList("EUR", "USD");

    List<Balance> balances_valid = Arrays.asList(
            new Balance(1L, "EUR", new BigDecimal("0.00")),
            new Balance(2L, "USD", new BigDecimal("0.00"))
    );


    @Test
    public void whenGetAccountById_thenAccountShouldBeFound() {
        Account account_valid = new Account();
        account_valid.setAccountId(accountId_valid);
        account_valid.setCustomerId(customerId_valid);
        account_valid.setCountry(country_valid);
        account_valid.setBalances(balances_valid);

        when(accountRepository.findAccountById(accountId_valid)).thenReturn(account_valid);
        Assertions.assertEquals(account_valid, accountService.getAccountById(accountId_valid));
        Assertions.assertEquals(accountId_valid, accountService.getAccountById(accountId_valid).getAccountId());
        Assertions.assertEquals(customerId_valid, accountService.getAccountById(accountId_valid).getCustomerId());
        Assertions.assertEquals(country_valid, accountService.getAccountById(accountId_valid).getCountry());
    }

    @Test
    public void whenGetAccountByInvalidId_thenAccountShouldNotBeFound_andReturnsError() {
        Account account_Invalid = new Account();
        account_Invalid.setAccountId(accountId_Invalid);
        account_Invalid.setCustomerId(customerId_valid);
        account_Invalid.setCountry(country_valid);
        account_Invalid.setBalances(balances_valid);

        when(accountRepository.findAccountById(accountId_Invalid)).thenReturn(account_Invalid);
        Assertions.assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(accountId_Invalid));
    }

    @Test
    public void whenAccountExists_getBalancesForAccount() {
        List<Long> balanceIds = balances_valid.stream().map(Balance::getBalanceId).collect(Collectors.toList());
        when(accountBalanceRepository.findBalanceIdsByAccountId(accountId_valid)).thenReturn(balanceIds);
        when(balanceRepository.findBalanceById(1L)).thenReturn(balances_valid.get(0));
        when(balanceRepository.findBalanceById(2L)).thenReturn(balances_valid.get(1));

        List<Balance> balances = accountService.getBalancesForAccount(accountId_valid);
        Assertions.assertEquals(balances_valid, balances);
        Assertions.assertEquals(balances_valid.get(0), balances.get(0));
        Assertions.assertEquals(balances_valid.get(1), balances.get(1));
    }

    @Test
    public void whenAccountDoesNotExist_andIDIncorrect_thenReturnsError() {
        List<Long> balanceIds = balances_valid.stream().map(Balance::getBalanceId).collect(Collectors.toList());
        when(accountBalanceRepository.findBalanceIdsByAccountId(accountId_Invalid)).thenReturn(balanceIds);
        when(balanceRepository.findBalanceById(1L)).thenReturn(balances_valid.get(0));
        when(balanceRepository.findBalanceById(2L)).thenReturn(balances_valid.get(1));

        Assertions.assertThrows(AccountNotFoundException.class, () -> accountService.getBalancesForAccount(accountId_Invalid));
    }

    @Test
    public void whenAccountDoesNotExist_thenCreateAnAccount() {
        Account account_valid = new Account();
        account_valid.setAccountId(accountId_valid);
        account_valid.setCustomerId(customerId_valid);
        account_valid.setCountry(country_valid);
        account_valid.setBalances(balances_valid);

        when(accountRepository.findAccountById(accountId_valid)).thenReturn(account_valid);
        when(accountRepository.insertAccount(account_valid)).thenReturn(account_valid);

        Account account = accountService.createAccount(customerId_valid, country_valid, currencies_valid);
        Assertions.assertEquals(customerId_valid, account.getCustomerId());
        Assertions.assertEquals(country_valid, account.getCountry());
    }
}
