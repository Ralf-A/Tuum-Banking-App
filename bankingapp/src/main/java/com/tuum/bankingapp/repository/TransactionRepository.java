package com.tuum.bankingapp.repository;

import com.tuum.bankingapp.model.Transaction;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TransactionRepository {
    // MyBatis annotations to define the SQL query for finding transactions by account ID
    @Select("SELECT * FROM transactions WHERE account_id = #{accountId}")
    List<Transaction> findTransactionsByAccountId(@Param("accountId") Long accountId);

    @Insert("INSERT INTO transactions (account_id, amount, currency, direction, description, balance_after_transaction) " +
            "VALUES (#{accountId}, #{amount}, #{currency}, #{direction}, #{description}, #{balanceAfterTransaction})")
    @SelectKey(statement = "SELECT lastval()", keyProperty = "transactionId", before = false, resultType = Long.class)
    Long createTransaction(Transaction transaction);
}