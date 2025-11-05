package com.opom.bankingapp.features.account.service;

import com.opom.bankingapp.dto.account.AccountAdminResponse;
import com.opom.bankingapp.dto.account.AccountListAdminResponse;

public interface AccountAdminService {
    AccountListAdminResponse getAllAccounts();
    AccountAdminResponse getAccountById(Long accountId);
}
