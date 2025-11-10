package com.opom.bankingapp.dto.transfer;

import com.opom.bankingapp.dto.user.AccountDetailResponse;

public class ValidateTransferResponse {
    private AccountDetailResponse fromAccountDetails;
    private AccountDetailResponse toAccountDetails;

    public ValidateTransferResponse() {
    }

    public ValidateTransferResponse(AccountDetailResponse fromAccountDetails, AccountDetailResponse toAccountDetails) {
        this.fromAccountDetails = fromAccountDetails;
        this.toAccountDetails = toAccountDetails;
    }

    public AccountDetailResponse getFromAccountDetails() {
        return fromAccountDetails;
    }
    public void setFromAccountDetails(AccountDetailResponse fromAccountDetails) {
        this.fromAccountDetails = fromAccountDetails;
    }
    public AccountDetailResponse getToAccountDetails() {
        return toAccountDetails;
    }
    public void setToAccountDetails(AccountDetailResponse toAccountDetails) {
        this.toAccountDetails = toAccountDetails;
    }
}
