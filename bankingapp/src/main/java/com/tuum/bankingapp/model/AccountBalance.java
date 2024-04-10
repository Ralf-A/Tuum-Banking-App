package com.tuum.bankingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalance {
    private Long accountBalanceId;
    private Long accountId;
    private Long balanceId;
}
