package com.opom.bankingapp.dto.user;

import com.opom.bankingapp.dto.common.OptionDto;
import java.time.LocalDate;

public record ProfileDetailsDto(
    String fullname,
    LocalDate dateOfBirth,
    String phoneNumber,
    String address,
    OptionDto gender,
    OptionDto nationality,
    boolean isPolicyAgreement,
    boolean isAutoSaveReceipt,
    boolean isFirstTimeLogin,
    boolean hasInitialPin,
    Long selectedAccountId
) {}
