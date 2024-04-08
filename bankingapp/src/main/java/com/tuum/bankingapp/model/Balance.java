package com.tuum.bankingapp.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Balance {
    private Long accountId;
    private BigDecimal availableAmount;
    private String currency;
}

