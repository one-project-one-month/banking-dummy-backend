package com.opom.bankingapp.repository;

import com.opom.bankingapp.dto.user.RecentTransfer;
import java.util.List;

public interface TransactionRepository {
    List<RecentTransfer> findRecentTransfersByUserId(Long userId);
}
