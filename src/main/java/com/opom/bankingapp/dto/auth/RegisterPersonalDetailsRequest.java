package com.opom.bankingapp.dto.auth;

import java.time.LocalDate;

// Based on your openapi.yaml
public record RegisterPersonalDetailsRequest(
    String email,
    String fullname,
    LocalDate dateOfBirth,
    int genderId,
    int nationalityId,
    String kycType,
    String kycData
) {}
