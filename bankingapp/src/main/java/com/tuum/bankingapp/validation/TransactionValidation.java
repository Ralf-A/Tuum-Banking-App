package com.tuum.bankingapp.validation;

import java.math.BigDecimal;

public interface TransactionValidation {
    void validateTransaction(Long accountId, Double amount, String currency, String transactionType, String description);
    void isCurrencyValid(String currency);
    void isAmountValid(Double amount);
    void isTransactionTypeValid(String transactionType);
    void isDescriptionValid(String description);
    void isAccountIdValid(Long accountId);
    void isValidCurrentBalance(BigDecimal currentBalance, Long accountId, String currency);
    void isValidSufficientFunds(BigDecimal newBalance, String direction);
}
