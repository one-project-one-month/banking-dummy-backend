package com.opom.bankingapp.repository;

import com.opom.bankingapp.dto.user.AccountDetailResponse;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Optional<Double> findBalanceByUserId(long userId);
    List<AccountDetailResponse> findAccountsByUserId(Long userId);
    Optional<Double> findBalanceByAccountId(Long accountId);
    void updateBalance(Long accountId, double newBalance);
    Optional<AccountDetailResponse> findAccountDetailsById(Long accountId);
    long createAccount(long userId, String accountNumber, int accountTypeId, double initialBalance, long createdBy);
}
