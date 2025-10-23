package com.opom.bankingapp.service.impl;

import com.opom.bankingapp.dto.transfer.AccountNumberPrepareRequest;
import com.opom.bankingapp.dto.transfer.NicknamePrepareRequest;
import com.opom.bankingapp.dto.transfer.TransferPrepareResponse;
import com.opom.bankingapp.exception.ResourceNotFoundException;
import com.opom.bankingapp.repository.TransferRepository;
import com.opom.bankingapp.service.TransferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;

    public TransferServiceImpl(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public TransferPrepareResponse prepareNicknameTransfer(Long fromUserId, NicknamePrepareRequest request) {

        return transferRepository.findNicknamePrepareDetails(
            (long) request.nicknameId(),
            fromUserId
        ).orElseThrow(() -> new ResourceNotFoundException("Nickname not found or does not belong to user"));
    }

    @Override
    @Transactional(readOnly = true)
    public TransferPrepareResponse prepareAccountNumberTransfer(AccountNumberPrepareRequest request) {

        return transferRepository.findAccountPrepareDetails(
                request.toAccountNumber()
        ).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }
}
