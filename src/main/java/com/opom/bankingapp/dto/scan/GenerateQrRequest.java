package com.opom.bankingapp.dto.scan;

public record GenerateQrRequest(
    double amount,
    String note
) {}
