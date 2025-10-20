package com.opom.bankingapp.service;

import com.opom.bankingapp.dto.user.FromAccountsResponse;
import com.opom.bankingapp.dto.user.RecentTransferListResponse;
import com.opom.bankingapp.dto.user.UserDetailsResponse;
import com.opom.bankingapp.model.UserPrincipal;

public interface UserService {
    void setPin(Long userId, String pin);
    void agreeToPolicy(Long userId, boolean agreement);
    UserDetailsResponse getUserDetails(UserPrincipal user);
    FromAccountsResponse getFromAccounts(Long userId);
    RecentTransferListResponse getRecentTransfers(Long userId);
    void setAutoSaveReceipt(Long userId, boolean flag);
    void changePassword(Long userId, String oldPassword, String newPassword);
    void verifyPin(Long userId, String oldPin);
    void switchAccount(Long userId, int accountId);
}
