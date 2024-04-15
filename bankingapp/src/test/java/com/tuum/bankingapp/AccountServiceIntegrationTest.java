package com.tuum.bankingapp;
import com.tuum.bankingapp.messaging.MessagePublisher;
import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.model.Balance;
import com.tuum.bankingapp.repository.AccountBalanceRepository;
import com.tuum.bankingapp.repository.AccountRepository;
import com.tuum.bankingapp.repository.BalanceRepository;
import com.tuum.bankingapp.service.AccountService;
import com.tuum.bankingapp.validation.AccountValidation;
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

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class AccountServiceIntegrationTest {
    @Autowired
    private AccountService accountService;

    @MockBean
    private MessagePublisher messagePublisher;
    @MockBean
    private BalanceRepository balanceRepository;
    @MockBean
    private AccountValidation accountValidation;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private AccountBalanceRepository accountBalanceRepository;

    @Test
    public void whenGetAccountById_thenAccountShouldBeFound() {
        //
    }

    @Test
    public void whenGetAccountByInvalidId_thenAccountShouldNotBeFound_andReturnsError() {
        //
    }

    @Test
    public void whenAccountExists_getBalancesForAccount() {
        //
    }

    @Test
    public void whenAccountDoesNotExist_thenGetBalancesForAccount_andReturnsError() {
        //
    }

    @Test
    public void whenAccountDoesNotExist_andIDIncorrect_thenReturnsError() {
        //
    }

    @Test
    public void whenAccountDoesNotExist_thenCreateAnAccount() {
        //
    }

    @Test
    public void whenAccountExists_thenCreateAnAccount_andReturnsError() {
        //
    }

    @Test
    public void whenInvalidCustomerID_thenCreateAnAccount_andReturnsError() {
        //
    }

    @Test
    public void whenInvalidCountry_thenCreateAnAccount_andReturnsError() {
        //
    }

    @Test
    public void whenInvalidCurrencies_thenCreateAnAccount_andReturnsError() {
        //
    }
}
