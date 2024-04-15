package com.tuum.bankingapp.validation;

public interface TransactionValidation {
    boolean isCurrencyValid(String currency);
    boolean isAmountValid(Double amount);
    boolean isTransactionTypeValid(String transactionType);
    boolean isDescriptionValid(String description);
    boolean isAccountIdValid(Long accountId);
}
