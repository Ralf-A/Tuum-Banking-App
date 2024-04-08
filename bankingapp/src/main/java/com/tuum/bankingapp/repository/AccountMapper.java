package com.tuum.bankingapp.repository;

import com.tuum.bankingapp.model.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountMapper {
    // MyBatis annotations to define the SQL query for finding accounts by customer ID
    @Select("SELECT * FROM accounts WHERE customer_id = #{customerId}")
    List<Account> findAccountsByCustomerId(@Param("customerId") Long customerId);
}