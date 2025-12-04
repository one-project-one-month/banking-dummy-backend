package com.opom.bankingapp.service;

import java.util.List;

import com.opom.bankingapp.dto.admin.AccountActionRequest;
import com.opom.bankingapp.dto.admin.DepositRequest;
import com.opom.bankingapp.dto.admin.DepositResponse;
import com.opom.bankingapp.dto.admin.UserListAdminResponse;

public interface AdminUserService {
    void processAccountAction(Long adminId, Long userId, AccountActionRequest request);
    UserListAdminResponse getAllUsers();
    void createDeposit(Long createdBy, DepositRequest request);
    List<DepositResponse> getAllDeposits();
}
