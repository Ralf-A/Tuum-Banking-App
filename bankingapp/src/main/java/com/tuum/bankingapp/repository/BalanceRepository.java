package com.tuum.bankingapp.repository;
import com.tuum.bankingapp.model.Balance;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BalanceRepository {

    @Select("SELECT * FROM balances WHERE account_id = #{accountId}")
    List<Balance> findBalancesByAccountId(@Param("accountId") Long accountId);

    @Insert("INSERT INTO balances(account_id, available_amount, currency) VALUES(#{accountId}, #{availableAmount}, #{currency})")
    @Options(useGeneratedKeys = true, keyProperty = "balanceId")
    void insertBalance(Balance balance);

    @Update("UPDATE balances SET available_amount = #{availableAmount} WHERE balance_id = #{balanceId}")
    void updateBalance(Balance balance);
}
