package com.opom.bankingapp.service;

import com.opom.bankingapp.dto.transfer.*;

public interface TransferService {
    TransferPrepareResponse prepareNicknameTransfer(Long fromUserId, NicknamePrepareRequest request);
    TransferPrepareResponse prepareAccountNumberTransfer(AccountNumberPrepareRequest request);
    ValidateTransferResponse validateTransfer(Long fromUserId, ValidateTransferRequest request);
}
