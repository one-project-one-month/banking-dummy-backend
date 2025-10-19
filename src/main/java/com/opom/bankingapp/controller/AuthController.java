package com.opom.bankingapp.controller;

import com.opom.bankingapp.dto.auth.AuthResponse;
import com.opom.bankingapp.dto.auth.LoginRequest;
import com.opom.bankingapp.dto.auth.PersonalDetailsTemplateResponse;
import com.opom.bankingapp.dto.auth.RegisterPersonalDetailsRequest;
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

    @GetMapping("/register/personal-details/template")
    public ResponseEntity<PersonalDetailsTemplateResponse> getPersonalDetailsTemplate() {
        PersonalDetailsTemplateResponse response = authService.getPersonalDetailsTemplate();
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
