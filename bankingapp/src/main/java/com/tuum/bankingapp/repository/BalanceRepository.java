package com.tuum.bankingapp.repository;
import com.tuum.bankingapp.model.Balance;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Repository
public interface BalanceRepository {

    // Method to find a balance by balance_id
    @Select("SELECT balance_id, currency, available_amount FROM balances WHERE balance_id = #{balanceId}")
    @Results({
            @Result(property = "balanceId", column = "balance_id"),
            @Result(property = "currency", column = "currency"),
            @Result(property = "availableAmount", column = "available_amount")
    })
    Balance findBalanceById(@Param("balanceId") Long balanceId);

    // Method to find available amount by account_id and currency
    @Select("SELECT b.available_amount FROM balances b " +
            "INNER JOIN account_balances ab ON b.balance_id = ab.balance_id " +
            "WHERE ab.account_id = #{accountId} AND b.currency = #{currency}")
    BigDecimal findAvailableAmountByAccountIdAndCurrency(@Param("accountId") Long accountId, @Param("currency") String currency);

    // Method to update available amount by account_id and currency
    @Update("UPDATE balances b SET available_amount = #{newBalance} " +
            "FROM account_balances ab " +
            "WHERE b.balance_id = ab.balance_id AND ab.account_id = #{accountId} AND b.currency = #{currency}")
    void updateAvailableAmountByAccountIdAndCurrency(@Param("newBalance") BigDecimal newBalance,
                                                     @Param("accountId") Long accountId,
                                                     @Param("currency") String currency);

    // Method to find balances by account_id
    @Select("SELECT b.* FROM balances b INNER JOIN account_balances ab ON b.balance_id = ab.balance_id WHERE ab.account_id = #{accountId}")
    List<Balance> findBalancesByAccountId(@Param("accountId") Long accountId);

    // Method to insert a new balance record
    @Insert("INSERT INTO balances (available_amount, currency) VALUES (#{availableAmount}, #{currency})")
    @Options(useGeneratedKeys = true, keyProperty = "balanceId")
    void insertBalance(Balance balance);
}

