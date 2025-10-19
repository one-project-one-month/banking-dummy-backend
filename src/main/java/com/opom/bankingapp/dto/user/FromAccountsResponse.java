package com.opom.bankingapp.dto.user;

import java.util.List;

public record FromAccountsResponse(
    List<AccountDetailResponse> fromAccountsOptions
) {}
