package com.tuum.bankingapp.model;

import lombok.*;

import java.math.BigDecimal;

/*
* Transaction Model
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {
    private Long accountId;
    private Long transactionId;
    private BigDecimal amount;
    private String currency;
    private String direction;
    private String description;
    private BigDecimal balanceAfterTransaction;
}

