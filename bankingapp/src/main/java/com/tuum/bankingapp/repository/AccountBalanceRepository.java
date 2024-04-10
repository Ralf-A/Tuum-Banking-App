package com.tuum.bankingapp.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AccountBalanceRepository {

    // Method to find balance_ids by account_id
    @Select("SELECT balance_id FROM account_balances WHERE account_id = #{accountId}")
    List<Long> findBalanceIdsByAccountId(@Param("accountId") Long accountId);

    // Method to insert a new account_balance record
    @Insert("INSERT INTO account_balances (account_id, balance_id) VALUES (#{accountId}, #{balanceId})")
    void insertAccountBalance(@Param("accountId") Long accountId, @Param("balanceId") Long balanceId);
}
