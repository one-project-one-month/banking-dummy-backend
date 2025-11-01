package com.opom.bankingapp.controller.auth;

import com.opom.bankingapp.dto.auth.*;
import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {
    private final AuthService authService;

    public RegisterController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register/personal-details/template")
    public ResponseEntity<PersonalDetailsTemplateResponse> getPersonalDetailsTemplate() {
        PersonalDetailsTemplateResponse response = authService.getPersonalDetailsTemplate();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/email/verify")
    public ResponseEntity<ApiResponse<String>> verifyEmail(
            @RequestBody EmailVerificationRequest request) {

        String generatedOtp = authService.verifyEmail(request);

        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "OTP sent successfully",
                generatedOtp
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/otp/verify")
    public ResponseEntity<ApiResponse<OtpVerificationResponse>> verifyOtp(
            @RequestBody OtpVerificationRequest request) {

        OtpVerificationResponse authData = authService.verifyOtp(request);

        ApiResponse<OtpVerificationResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "OTP verified successfully",
                authData
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/personal-details")
    public ResponseEntity<ApiResponse<AuthResponse>> registerPersonalDetails(
            @RequestBody RegisterPersonalDetailsRequest request) {

        AuthResponse authData = authService.registerPersonalDetails(request);

        ApiResponse<AuthResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Personal details registered successfully",
                authData
        );
        return ResponseEntity.ok(response);
    }
}
