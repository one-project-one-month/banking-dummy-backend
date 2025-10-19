package com.opom.bankingapp.dto.user;

public record RecentTransfer(
    UserSummary user,
    AccountDetailResponse account
) {}
