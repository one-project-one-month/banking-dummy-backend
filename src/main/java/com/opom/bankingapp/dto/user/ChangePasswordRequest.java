package com.opom.bankingapp.dto.user;

public record ChangePasswordRequest(
    String oldPassword,
    String newPassword
) {}
