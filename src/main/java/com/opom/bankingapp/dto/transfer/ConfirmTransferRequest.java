package com.opom.bankingapp.dto.transfer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ConfirmTransferRequest(
    @NotNull(message = "To Account ID is required")
    Integer toAccountId,

    @Positive(message = "Amount must be positive")
    double amount,
    
    String note,
    
    @NotBlank(message = "PIN is required")
    String pin
) {}
