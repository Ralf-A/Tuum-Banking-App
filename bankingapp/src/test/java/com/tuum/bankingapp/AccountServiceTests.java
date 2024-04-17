package com.tuum.bankingapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tuum.bankingapp.exception.AccountNotFoundException;
import com.tuum.bankingapp.messaging.MessagePublisher;
import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.model.Balance;
import com.tuum.bankingapp.repository.AccountBalanceRepository;
import com.tuum.bankingapp.repository.AccountRepository;
import com.tuum.bankingapp.repository.BalanceRepository;
import com.tuum.bankingapp.service.AccountService;
import com.tuum.bankingapp.validation.AccountCreationValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.List;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountBalanceRepository accountBalanceRepository;
    @Mock
    private BalanceRepository balanceRepository;
    @Mock
    private MessagePublisher messagePublisher;
    @Mock
    private AccountCreationValidation accountCreationValidation;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService(messagePublisher, balanceRepository, accountCreationValidation, accountRepository, accountBalanceRepository);
    }

    @Test
    public void addAccountTest() {
        Account testAccount = new Account();
        testAccount.setCustomerId(123L);
        testAccount.setAccountId(123L);
        testAccount.setCountry("US");
        testAccount.setBalances(List.of(new Balance(456L, "USD", BigDecimal.ZERO)));

        accountRepository.insertAccount(any(Account.class));
        accountService.createAccount(123L, "US", List.of("USD"));

        verify(accountRepository, times(1)).insertAccount(any(Account.class));
    }

    @Test
    public void getAccountByIdTest() {
        Account testAccount = new Account();
        testAccount.setAccountId(123L);
        testAccount.setCustomerId(123L);
        testAccount.setCountry("US");
        testAccount.setBalances(List.of(new Balance(456L, "USD", BigDecimal.ZERO)));

        when(accountRepository.findAccountById(123L)).thenReturn(testAccount);
        Account result = accountService.getAccountById(123L);

        assertEquals(123L, result.getAccountId());
        verify(accountRepository, times(1)).findAccountById(123L);
    }

    @Test
    public void getAccountByInvalidIdTest() {
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccountById(-1L);
        });
    }
}


