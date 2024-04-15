package com.tuum.bankingapp.dto;

import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.model.Balance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    /**
     * Account Data Transfer Object for a POST request to create an account
     */
    private Long customerId;
    private String country;
    private List<String> currency;
}

