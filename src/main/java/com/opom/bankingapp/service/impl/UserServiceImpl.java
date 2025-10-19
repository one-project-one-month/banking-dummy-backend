package com.opom.bankingapp.service.impl;

import com.opom.bankingapp.dto.user.FromAccountsResponse;
import com.opom.bankingapp.dto.user.RecentTransferListResponse;
import com.opom.bankingapp.dto.user.UserDetailsResponse;
import com.opom.bankingapp.service.UserService;
import com.opom.bankingapp.model.UserPrincipal;
import com.opom.bankingapp.repository.AccountRepository;
import com.opom.bankingapp.repository.TransactionRepository;
import com.opom.bankingapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           AccountRepository accountRepository,
                           TransactionRepository transactionRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void setPin(Long userId, String pin) {
        String hashedPin = passwordEncoder.encode(pin);
        userRepository.updatePin(userId, hashedPin);
    }

    @Override
    @Transactional
    public void agreeToPolicy(Long userId, boolean agreement) {
        userRepository.updatePolicyAgreement(userId, agreement);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsResponse getUserDetails(UserPrincipal user) {
        double balance = accountRepository.findBalanceByUserId(user.getId())
                .orElse(0.0);

        return new UserDetailsResponse(user.getEmail(), user.getUsername(), balance);
    }

    @Override
    @Transactional(readOnly = true)
    public FromAccountsResponse getFromAccounts(Long userId) {
        var accounts = accountRepository.findAccountsByUserId(userId);
        return new FromAccountsResponse(accounts);
    }

    @Override
    @Transactional(readOnly = true)
    public RecentTransferListResponse getRecentTransfers(Long userId) {
        var transfers = transactionRepository.findRecentTransfersByUserId(userId);
        return new RecentTransferListResponse(transfers);
    }
}