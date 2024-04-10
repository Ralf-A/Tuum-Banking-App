package com.tuum.bankingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    private String customerId;
    private String country;
    private List<String> currency;
}

