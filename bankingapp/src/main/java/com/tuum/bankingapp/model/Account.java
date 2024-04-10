package com.tuum.bankingapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long accountId;
    private Long customerId;
    private String country;
    private List<Balance> balances;
    private List<String> currencies;
}



