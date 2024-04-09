package com.tuum.bankingapp.repository;

import com.tuum.bankingapp.model.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TransactionRepository {
    // MyBatis annotations to define the SQL query for finding transactions by account ID
    @Select("SELECT * FROM transactions WHERE account_id = #{accountId}")
    List<Transaction> findTransactionsByAccountId(@Param("accountId") Long accountId);
}