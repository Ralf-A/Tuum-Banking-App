package com.tuum.bankingapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Balance {
    private BigDecimal availableAmount;
    private String currency;
}

