package com.tuum.bankingapp.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long accountId;
    private BigDecimal amount;
    private String currency;
    private String direction;
    private String Description;
}
