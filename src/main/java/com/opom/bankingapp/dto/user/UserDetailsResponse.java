package com.opom.bankingapp.dto.user;

import com.opom.bankingapp.dto.common.OptionDto;

import java.time.LocalDate;

public record UserDetailsResponse(
    String email,
    String username,
    String fullname,
    LocalDate dateOfBirth,
    OptionDto gender,
    OptionDto nationality,
    boolean isPolicyAgreement,
    boolean isAutoSaveReceipt,
    double currentBalance,
    AccountDetailResponse selectedAccountDetails
) {}
