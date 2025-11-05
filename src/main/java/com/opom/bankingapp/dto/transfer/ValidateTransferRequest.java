package com.opom.bankingapp.dto.transfer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ValidateTransferRequest(
    @NotNull(message = "To Account ID is required")
    Integer toAccountId,
    
    @NotBlank(message = "PIN is required")
    String pin
) {}
