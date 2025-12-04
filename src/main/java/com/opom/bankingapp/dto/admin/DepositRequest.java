package com.opom.bankingapp.dto.admin;

import com.opom.bankingapp.model.TransactionCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DepositRequest(
        @NotNull
        Long accountId,
        @NotNull
        Float amount,
        @NotNull
        TransactionCategory transactionType
) {}
