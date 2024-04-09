package com.tuum.bankingapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long accountId;
    private Long customerId;
    private String country;
    private Map<String, Balance> balances;
}

