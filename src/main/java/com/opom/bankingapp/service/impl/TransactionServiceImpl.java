package com.opom.bankingapp.service.impl;

import com.opom.bankingapp.dto.scan.QrTokenPayload;
import com.opom.bankingapp.dto.scan.ScanToPayRequest;
import com.opom.bankingapp.exception.ResourceNotFoundException;
import com.opom.bankingapp.repository.AccountRepository;
import com.opom.bankingapp.repository.TransactionRepository;
import com.opom.bankingapp.repository.UserRepository;
import com.opom.bankingapp.service.TokenService;
import com.opom.bankingapp.service.TransactionService;
import com.opom.bankingapp.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TokenService tokenService;
    private final UserService userService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionServiceImpl(TokenService tokenService,
                                  UserService userService,
                                  AccountRepository accountRepository,
                                  TransactionRepository transactionRepository,
                                  UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void executeScanToPay(Long payerUserId, ScanToPayRequest request) {
        userService.verifyPin(payerUserId, request.pin());

        QrTokenPayload payload = tokenService.decode(request.token(), QrTokenPayload.class)
                .orElseThrow(() -> new BadCredentialsException("Invalid or expired QR token."));

        Long fromAccountId = userRepository.findSelectedAccountIdByUserId(payerUserId)
                .orElseThrow(() -> new BadCredentialsException("No selected account found for user."));
        Long toAccountId = payload.toAccountId();
        double amount = payload.amount();

        if (amount <= 0) {
            throw new BadCredentialsException("Transaction amount must be positive.");
        }
        
        if (fromAccountId == toAccountId) {
            throw new BadCredentialsException("Cannot transfer to the same account.");
        }

        double fromBalance = accountRepository.findBalanceByAccountId(fromAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Payer account not found."));

        if (fromBalance < amount) {
            throw new BadCredentialsException("Insufficient funds.");
        }

        double toBalance = accountRepository.findBalanceByAccountId(toAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found."));

        accountRepository.updateBalance(fromAccountId, fromBalance - amount);
        accountRepository.updateBalance(toAccountId, toBalance + amount);

        transactionRepository.saveTransaction(fromAccountId, toAccountId, amount, payerUserId);
    }
}
