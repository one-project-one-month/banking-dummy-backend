package com.opom.bankingapp.service;

import com.opom.bankingapp.dto.auth.AuthResponse;
import com.opom.bankingapp.dto.auth.LoginRequest;
import com.opom.bankingapp.dto.auth.PersonalDetailsTemplateResponse;
import com.opom.bankingapp.dto.auth.RegisterPersonalDetailsRequest;

public interface AuthService {
    PersonalDetailsTemplateResponse getPersonalDetailsTemplate();
    AuthResponse registerPersonalDetails(RegisterPersonalDetailsRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String authHeader);
}
