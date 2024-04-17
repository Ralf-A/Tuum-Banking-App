package com.tuum.bankingapp;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Suite
@SelectClasses({
        AccountServiceTests.class,
        TransactionServiceTests.class
})
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BankingApplicationTests {
}
