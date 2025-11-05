package com.opom.bankingapp.dto.transfer;

import com.opom.bankingapp.dto.user.AccountDetailResponse;

public record ValidateTransferResponse(
    AccountDetailResponse fromAccountDetails,
    AccountDetailResponse toAccountDetails
) {}
