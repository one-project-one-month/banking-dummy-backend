package com.opom.bankingapp.features.account.repository;

import com.opom.bankingapp.dto.account.AccountAdminResponse;
import java.util.List;
import java.util.Optional;

public interface AccountAdminRepository {
    List<AccountAdminResponse> findAllAccounts();
    Optional<AccountAdminResponse> findAccountById(Long accountId);
}
