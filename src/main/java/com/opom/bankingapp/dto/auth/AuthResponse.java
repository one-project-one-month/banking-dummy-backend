package com.opom.bankingapp.dto.auth;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    String email,
    String username,
    double currentBalance
) {}
