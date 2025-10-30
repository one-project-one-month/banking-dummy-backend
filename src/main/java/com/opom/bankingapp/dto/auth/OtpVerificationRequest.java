package com.opom.bankingapp.dto.auth;

public record OtpVerificationRequest(String email, String otp) {}
