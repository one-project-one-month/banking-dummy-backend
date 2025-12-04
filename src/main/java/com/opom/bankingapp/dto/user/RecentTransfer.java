package com.opom.bankingapp.dto.user;

import com.opom.bankingapp.model.TransactionType;
import java.math.BigDecimal;
import java.sql.Timestamp;

public record RecentTransfer(
        Long id,
        Long transactionId,
        BigDecimal amount,
        TransactionType transactionType,
        boolean status,
        Timestamp createdAt,
        Timestamp updatedAt,
        UserSummary user,
        AccountDetailResponse account,
        boolean isIncome
) {}
