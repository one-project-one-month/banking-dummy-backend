package com.opom.bankingapp.service.impl;

import com.opom.bankingapp.dto.admin.AccountActionRequest;
import com.opom.bankingapp.dto.admin.ActionType;
import com.opom.bankingapp.dto.admin.AdminApprovalDetails;
import com.opom.bankingapp.exception.ResourceNotFoundException;
import com.opom.bankingapp.model.UserStatus;
import com.opom.bankingapp.repository.AccountRepository;
import com.opom.bankingapp.repository.UserRepository;
import com.opom.bankingapp.service.AdminUserService;
import com.opom.bankingapp.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public AdminUserServiceImpl(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public void processAccountAction(Long adminId, Long userId, AccountActionRequest request) {
        AdminApprovalDetails userDetails = userRepository.findApprovalDetailsById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        int newStatus;
        String actionMessage;

        if (request.action() == ActionType.APPROVE) {
            newStatus = UserStatus.ACTIVE.getCode(); // 2
            actionMessage = "approved";

            String newRawPassword = UUID.randomUUID().toString().substring(0, 8);
            String newHashedPassword = passwordEncoder.encode(newRawPassword);

            userRepository.updatePassword(userId, newHashedPassword);

            String newAccountNumber = String.format("ACC%07d", System.currentTimeMillis() % 10000000L);
            int defaultAccountTypeId = 1;
            double initialBalance = 0.0;

            long newAccountId = accountRepository.createAccount(userId, newAccountNumber, defaultAccountTypeId, initialBalance, adminId);

            userRepository.updateSelectedAccount(userId, (int) newAccountId);

            emailService.sendAccountApprovedEmail(userDetails.email(), userDetails.username(), newRawPassword);

        } else if (request.action() == ActionType.REJECT) {
            newStatus = UserStatus.BLOCKED.getCode(); // 4
            actionMessage = "rejected";
        } else {
            throw new IllegalArgumentException("Invalid action specified.");
        }

        userRepository.updateStatus(userId, newStatus);

        System.out.printf("---- ADMIN ACTION ----\nUser ID %d has been %s by Admin ID %d.\nNew Status: %s\n----------------------\n",
                userId, actionMessage, adminId, UserStatus.fromCode(newStatus).getValue());
    }
}
