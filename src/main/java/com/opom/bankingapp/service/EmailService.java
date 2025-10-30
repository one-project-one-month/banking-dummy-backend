package com.opom.bankingapp.service;

public interface EmailService {
    void sendOtpEmail(String toEmail, String otp);
}
