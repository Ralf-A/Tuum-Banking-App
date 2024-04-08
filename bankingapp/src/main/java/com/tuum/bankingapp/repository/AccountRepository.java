package com.tuum.bankingapp.repository;

import com.tuum.bankingapp.model.Account;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountRepository {
    @Select("SELECT * FROM accounts WHERE customer_id = #{customerId}")
    Account findAccountsByCustomerId(@Param("customerId") Long customerId);

    @Select("SELECT * FROM accounts WHERE account_id = #{accountId}")
    Account findAccountById(@Param("accountId") Long accountId);

    @Insert("INSERT INTO accounts (customer_id, country) VALUES (#{customerId}, #{country})")
    @Options(useGeneratedKeys = true, keyProperty = "accountId")
    void insertAccount(Account account);
}