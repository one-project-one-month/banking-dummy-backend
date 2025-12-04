package com.opom.bankingapp.dto.admin;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.opom.bankingapp.dto.user.AccountDetailResponse;
import com.opom.bankingapp.model.TransactionType;

public record DepositResponse(
        Long id,
        Long transactionId,
        Long accountId,
        BigDecimal amount,
        TransactionType transactionType,
        boolean status,
        Timestamp createdAt,
        Timestamp updatedAt,
        AccountDetailResponse accountDetail
) {}

