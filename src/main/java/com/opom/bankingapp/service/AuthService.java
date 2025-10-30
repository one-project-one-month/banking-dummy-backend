package com.opom.bankingapp.service;

import com.opom.bankingapp.dto.auth.*;

public interface AuthService {
    PersonalDetailsTemplateResponse getPersonalDetailsTemplate();
    void verifyEmail(EmailVerificationRequest request);
    OtpVerificationResponse verifyOtp(OtpVerificationRequest request);
    AuthResponse registerPersonalDetails(RegisterPersonalDetailsRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String authHeader);
}
