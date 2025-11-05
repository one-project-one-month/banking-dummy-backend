package com.opom.bankingapp.service.impl;

import com.opom.bankingapp.dto.transfer.*;
import com.opom.bankingapp.dto.user.AccountDetailResponse;
import com.opom.bankingapp.exception.ResourceNotFoundException;
import com.opom.bankingapp.repository.AccountRepository;
import com.opom.bankingapp.repository.TransferRepository;
import com.opom.bankingapp.repository.UserRepository;
import com.opom.bankingapp.service.TransferService;
import com.opom.bankingapp.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public TransferServiceImpl(TransferRepository transferRepository,
                               UserService userService,
                               UserRepository userRepository,
                               AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
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

    @Override
    @Transactional(readOnly = true)
    public ValidateTransferResponse validateTransfer(Long fromUserId, ValidateTransferRequest request) {
        userService.verifyPin(fromUserId, request.pin());

        Long fromAccountId = userRepository.findSelectedAccountIdByUserId(fromUserId)
                .orElseThrow(() -> new BadCredentialsException("No account selected. Please select an account to transfer from."));

        AccountDetailResponse fromAccountDetails = accountRepository.findAccountDetailsById(fromAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("From-account details not found for ID: " + fromAccountId));

        long toAccountId = request.toAccountId();
        AccountDetailResponse toAccountDetails = accountRepository.findAccountDetailsById(toAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("To-account not found with ID: " + toAccountId));

        return new ValidateTransferResponse(fromAccountDetails, toAccountDetails);
    }
}
