package com.opom.bankingapp.dto.user;

public record AccountDetailResponse(
    int id,
    String accountNumber,
    double balance
) {}
