package com.opom.bankingapp.service;

import com.opom.bankingapp.dto.scan.ScanToPayRequest;

public interface TransactionService {
    void executeScanToPay(Long payerUserId, ScanToPayRequest request);
}
