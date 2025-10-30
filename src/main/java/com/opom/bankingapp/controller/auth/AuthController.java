package com.opom.bankingapp.controller.auth;

import com.opom.bankingapp.dto.auth.*;
import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @RequestHeader("Authorization") String authHeader) {

        AuthResponse authData = authService.refreshToken(authHeader);

        ApiResponse<AuthResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Access token refreshed successfully",
                authData
        );
        return ResponseEntity.ok(response);
    }
}
