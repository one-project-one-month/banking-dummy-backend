package com.opom.bankingapp.service;

import com.opom.bankingapp.dto.scan.ScanToPayRequest;
import com.opom.bankingapp.dto.transfer.ConfirmTransferRequest;

public interface TransactionService {
    void executeScanToPay(Long payerUserId, ScanToPayRequest request);
    void executeTransfer(Long payerUserId, ConfirmTransferRequest request); // ADD THIS LINE
}
