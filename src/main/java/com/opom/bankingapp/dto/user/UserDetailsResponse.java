package com.opom.bankingapp.dto.user;

public record UserDetailsResponse(
    String email,
    String username,
    double currentBalance
) {}
