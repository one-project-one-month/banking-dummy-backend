package com.opom.bankingapp.service;

import com.opom.bankingapp.dto.admin.AccountActionRequest;
import com.opom.bankingapp.dto.admin.UserListAdminResponse;

public interface AdminUserService {
    void processAccountAction(Long adminId, Long userId, AccountActionRequest request);
    UserListAdminResponse getAllUsers();
}
