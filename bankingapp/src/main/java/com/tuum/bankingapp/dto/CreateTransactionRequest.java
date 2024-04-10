package com.tuum.bankingapp.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequest {
    private String accountId;
    private String amount;
    private String currency;
    private String direction;
    private String Description;
}
