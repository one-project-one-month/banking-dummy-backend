package com.opom.bankingapp.dto.transfer;

public record TransferPrepareResponse(
    ToAccountDetails toAccountDetails,
    UserDetails userDetails
) {
    public record ToAccountDetails(
        int id,
        String accountNumber
    ) {}

    public record UserDetails(
        long id,
        String fullname
    ) {}
}
