package com.tuum.bankingapp.repository;

import com.tuum.bankingapp.model.Transaction;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Transaction objects interface that extends MyBatis Mapper interface.
 * Contains methods to interact with the transactions table in the database.
 */
@Mapper
@Repository
public interface TransactionRepository {
    // MyBatis annotations to define the SQL query for finding transactions by account ID
    @Select("SELECT transaction_id, account_id, amount, currency, direction, description, balance_after_transaction FROM transactions WHERE account_id = #{accountId}")
    @Results({
            @Result(property = "transactionId", column = "transaction_id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "currency", column = "currency"),
            @Result(property = "direction", column = "direction"),
            @Result(property = "description", column = "description"),
            @Result(property = "balanceAfterTransaction", column = "balance_after_transaction")
    })
    List<Transaction> findTransactionsByAccountId(@Param("accountId") Long accountId);

    @Insert("INSERT INTO transactions (account_id, amount, currency, direction, description, balance_after_transaction) " +
            "VALUES (#{accountId}, #{amount}, #{currency}, #{direction}, #{description}, #{balanceAfterTransaction})")
    @SelectKey(statement = "SELECT lastval()", keyProperty = "transactionId", before = false, resultType = Long.class)
    Long createTransaction(Transaction transaction);
}