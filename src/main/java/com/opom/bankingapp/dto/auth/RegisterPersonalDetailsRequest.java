package com.opom.bankingapp.dto.auth;

import java.time.LocalDate;

public record RegisterPersonalDetailsRequest(
    String verificationToken,
    String fullname,
    LocalDate dateOfBirth,
    int genderId,
    int nationalityId,
    String kycType,
    String kycData
) {}
