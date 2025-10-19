package com.opom.bankingapp.dto.user;

import java.util.List;

public record RecentTransferListResponse(
    List<RecentTransfer> recentTransferListOptions
) {}
