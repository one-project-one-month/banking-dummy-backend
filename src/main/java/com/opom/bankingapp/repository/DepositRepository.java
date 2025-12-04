package com.opom.bankingapp.repository;

import java.util.List;

import com.opom.bankingapp.dto.admin.DepositRequest;
import com.opom.bankingapp.dto.admin.DepositResponse;

public interface DepositRepository {
    Long createDeposit(Long createdBy, DepositRequest request);
    List<DepositResponse> findDepositsByUserId(Long userId);
}

