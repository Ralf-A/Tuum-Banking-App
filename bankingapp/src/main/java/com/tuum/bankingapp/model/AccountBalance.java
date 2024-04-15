package com.tuum.bankingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model to represent the relationship between an account and a balance - an account can have multiple balances.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalance {
    private Long accountBalanceId;
    private Long accountId;
    private Long balanceId;
}
