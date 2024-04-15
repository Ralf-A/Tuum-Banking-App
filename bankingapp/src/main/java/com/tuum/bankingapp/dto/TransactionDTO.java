package com.tuum.bankingapp.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    /**
     * Transaction Data Transfer Object for a POST request to create a transaction
     */
    private Long accountId;
    private BigDecimal amount;
    private String currency;
    private String direction;
    private String description;
}
