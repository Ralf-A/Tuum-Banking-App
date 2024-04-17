package com.tuum.bankingapp.repository;

import com.tuum.bankingapp.model.Account;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

/*
 * Account objects interface that extends MyBatis Mapper interface.
 * Contains methods to interact with the accounts table in the database.
 */
@Mapper
@Repository
public interface AccountRepository {

    // Method to find accounts by customer_id
    @Select("SELECT * FROM accounts WHERE customer_id = #{customerId}")
    List<Account> findAccountsByCustomerId(@Param("customerId") Long customerId);

    // Method to find account by account_id
    @Select("SELECT * FROM accounts WHERE account_id = #{accountId}")
    @Results({
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "country", column = "country"),
            @Result(property = "balances", javaType = List.class, column = "account_id",
                    many = @Many(select = "com.tuum.bankingapp.repository.BalanceRepository.findBalancesByAccountId"))
    })
    Account findAccountById(@Param("accountId") Long accountId);

    // Method to insert account by customer_id, country
    @Insert("INSERT INTO accounts (customer_id, country) VALUES (#{customerId}, #{country})")
    @Options(useGeneratedKeys = true, keyProperty = "accountId")
    Account insertAccount(Account account);
}

