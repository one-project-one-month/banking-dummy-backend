package com.opom.bankingapp.dto.scan;

public record QrTokenPayload(
    int toAccountId,
    double amount,
    String note
) {}
