package com.opom.bankingapp.controller.auth;

import com.opom.bankingapp.dto.auth.AuthResponse;
import com.opom.bankingapp.dto.auth.LoginRequest;
import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> userLogin(@RequestBody LoginRequest request) {
        AuthResponse authData = authService.login(request);

        ApiResponse<AuthResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "User logged in successfully",
                authData
        );
        return ResponseEntity.ok(response);
    }
}
