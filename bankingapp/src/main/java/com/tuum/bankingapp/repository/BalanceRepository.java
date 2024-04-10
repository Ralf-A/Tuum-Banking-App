package com.tuum.bankingapp.repository;
import com.tuum.bankingapp.model.Balance;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BalanceRepository {

    // Method to find a balance by balance_id
    @Select("SELECT * FROM balances WHERE balance_id = #{balanceId}")
    Balance findBalanceById(@Param("balanceId") Long balanceId);

    // Method to insert a new balance record
    @Insert("INSERT INTO balances (available_amount, currency) VALUES (#{availableAmount}, #{currency})")
    @Options(useGeneratedKeys = true, keyProperty = "balanceId")
    void insertBalance(Balance balance);

    // Method to update a balance record
    @Update("UPDATE balances SET available_amount = #{availableAmount} WHERE balance_id = #{balanceId}")
    void updateBalance(Balance balance);
}

