package com.opom.bankingapp.dto.scan;

public record QrTokenPayload(
    Long toAccountId,
    double amount,
    String note
) {}
