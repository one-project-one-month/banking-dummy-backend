package com.opom.bankingapp.repository;

import java.util.List;
import java.util.Optional;

import com.opom.bankingapp.dto.user.RecentTransfer;

public interface TransactionRepository {
    List<RecentTransfer> findRecentTransfersByUserId(Long userId);
    void saveTransaction(Long fromAccountId, Long toAccountId, double amount, Long createdBy);
    List<RecentTransfer> findTransactionHistoryByUserId(Long userId);
    Optional<Long> findTransactionById(Long id);
}
