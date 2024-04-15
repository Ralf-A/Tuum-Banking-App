package com.tuum.bankingapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/*
* Balance Model
*/
@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Balance {
    private Long balanceId;
    private String currency;
    private BigDecimal availableAmount;
}


