package com.opom.bankingapp.service;

import com.opom.bankingapp.dto.transfer.AccountNumberPrepareRequest;
import com.opom.bankingapp.dto.transfer.NicknamePrepareRequest;
import com.opom.bankingapp.dto.transfer.TransferPrepareResponse;

public interface TransferService {
    TransferPrepareResponse prepareNicknameTransfer(Long fromUserId, NicknamePrepareRequest request);
    TransferPrepareResponse prepareAccountNumberTransfer(AccountNumberPrepareRequest request);
}
