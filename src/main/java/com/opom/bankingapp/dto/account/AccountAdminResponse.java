package com.opom.bankingapp.dto.account;

import com.opom.bankingapp.dto.common.OptionDto;
import java.time.LocalDateTime;

public record AccountAdminResponse(
    Long id,
    String accountNumber,
    String accountHolder,
    OptionDto accountType,
    boolean status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long createdBy,
    Long updatedBy
) {}
