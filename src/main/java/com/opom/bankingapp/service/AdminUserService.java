package com.opom.bankingapp.service;

import com.opom.bankingapp.dto.admin.AccountActionRequest;

public interface AdminUserService {
    void processAccountAction(Long adminId, Long userId, AccountActionRequest request);
}
