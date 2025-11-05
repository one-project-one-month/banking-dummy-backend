package com.opom.bankingapp.features.account.service.impl;

import com.opom.bankingapp.dto.account.AccountAdminResponse;
import com.opom.bankingapp.dto.account.AccountListAdminResponse;
import com.opom.bankingapp.exception.ResourceNotFoundException;
import com.opom.bankingapp.features.account.repository.AccountAdminRepository;
import com.opom.bankingapp.features.account.service.AccountAdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountAdminServiceImpl implements AccountAdminService {

    private final AccountAdminRepository accountAdminRepository;

    public AccountAdminServiceImpl(AccountAdminRepository accountAdminRepository) {
        this.accountAdminRepository = accountAdminRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AccountListAdminResponse getAllAccounts() {
        return new AccountListAdminResponse(accountAdminRepository.findAllAccounts());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountAdminResponse getAccountById(Long accountId) {
        return accountAdminRepository.findAccountById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));
    }
}
