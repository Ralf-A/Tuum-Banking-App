package com.tuum.bankingapp.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * Model for an account
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long accountId;
    private Long customerId;
    private String country;
    private List<Balance> balances;
}



