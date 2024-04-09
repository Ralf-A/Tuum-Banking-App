package com.tuum.bankingapp.repository;

import com.tuum.bankingapp.model.Account;
import jdk.jfr.Registered;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AccountRepository {

    @Select("SELECT * FROM accounts WHERE customer_id = #{customerId}")
    @Results({
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "balances", javaType = List.class, column = "account_id",
                    many = @Many(select = "findBalancesByAccountId"))
    })
    Account findAccountsByCustomerId(@Param("customerId") Long customerId);

    @Select("SELECT * FROM accounts WHERE account_id = #{accountId}")
    @Results({
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "balances", javaType = List.class, column = "account_id",
                    many = @Many(select = "findBalancesByAccountId"))
    })
    Account findAccountById(@Param("accountId") Long accountId);

    @Insert("INSERT INTO accounts (customer_id, country) VALUES (#{customerId}, #{country})")
    @Options(useGeneratedKeys = true, keyProperty = "accountId")
    void insertAccount(Account account);
}