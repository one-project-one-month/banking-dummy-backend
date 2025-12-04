package com.opom.bankingapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opom.bankingapp.dto.admin.DepositResponse;
import com.opom.bankingapp.dto.user.AccountDetailResponse;
import com.opom.bankingapp.dto.user.FromAccountsResponse;
import com.opom.bankingapp.dto.user.ProfileDetailsDto;
import com.opom.bankingapp.dto.user.RecentTransferListResponse;
import com.opom.bankingapp.dto.user.UpdateProfileRequest;
import com.opom.bankingapp.dto.user.UserDetailsResponse;
import com.opom.bankingapp.exception.ResourceNotFoundException;
import com.opom.bankingapp.model.UserPrincipal;
import com.opom.bankingapp.repository.AccountRepository;
import com.opom.bankingapp.repository.DepositRepository;
import com.opom.bankingapp.repository.TransactionRepository;
import com.opom.bankingapp.repository.UserRepository;
import com.opom.bankingapp.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepositRepository depositRepository;
    
    public UserServiceImpl(UserRepository userRepository,
                           AccountRepository accountRepository,
                           TransactionRepository transactionRepository,
                           PasswordEncoder passwordEncoder,
                           DepositRepository depositRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
        this.depositRepository = depositRepository;
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
        Optional<Long> selectedAccountIdOpt = userRepository.findSelectedAccountIdByUserId(user.getId());

        ProfileDetailsDto profile = userRepository.findProfileDetailsByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user ID: " + user.getId()));

        AccountDetailResponse selectedAccountDetails = selectedAccountIdOpt
                .flatMap(accountRepository::findAccountDetailsById)
                .orElse(null);

        double balance = Optional.ofNullable(selectedAccountDetails)
                .map(AccountDetailResponse::balance)
                .orElseGet(() -> accountRepository.findBalanceByUserId(user.getId()).orElse(0.0));

        return new UserDetailsResponse(
                user.getEmail(),
                user.getUsername(),
                profile.fullname(),
                profile.phoneNumber(),
                profile.address(),
                profile.dateOfBirth(),
                profile.gender(),
                profile.nationality(),
                profile.isPolicyAgreement(),
                profile.isAutoSaveReceipt(),
                profile.isFirstTimeLogin(),
                profile.hasInitialPin(),
                balance,
                selectedAccountDetails
        );
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

    @Override
    @Transactional
    public void setAutoSaveReceipt(Long userId, boolean flag) {
        userRepository.updateAutoSaveReceipt(userId, flag);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        String currentHashedPassword = userRepository.findHashedPasswordById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        if (!passwordEncoder.matches(oldPassword, currentHashedPassword)) {
            throw new BadCredentialsException("Incorrect old password");
        }

        String newHashedPassword = passwordEncoder.encode(newPassword);
        userRepository.updatePassword(userId, newHashedPassword);
    }

    @Override
    @Transactional(readOnly = true)
    public void verifyPin(Long userId, String oldPin) {
        String currentHashedPin = userRepository.findHashedPinById(userId)
                .orElseThrow(() -> new BadCredentialsException("PIN not set or user not found"));

        if (!passwordEncoder.matches(oldPin, currentHashedPin)) {
            throw new BadCredentialsException("Incorrect PIN");
        }
    }

    @Override
    @Transactional
    public void switchAccount(Long userId, int accountId) {
        boolean accountMatchesUser = accountRepository.findAccountsByUserId(userId)
                .stream()
                .anyMatch(acc -> acc.id() == accountId);

        if (!accountMatchesUser) {
            throw new BadCredentialsException("Account not found or does not belong to user");
        }

        userRepository.updateSelectedAccount(userId, accountId);
    }

    @Override
    @Transactional
    public UserDetailsResponse updateProfileDetails(UserPrincipal user, UpdateProfileRequest request) {
        userRepository.updateProfileDetails(user.getId(), request);

        return this.getUserDetails(user);
    }

    @Override
    @Transactional(readOnly = true)
    public RecentTransferListResponse getTransactionHistory(Long userId) {
        var transfers = transactionRepository.findTransactionHistoryByUserId(userId);
        return new RecentTransferListResponse(transfers);
    }

	@Override
	public List<DepositResponse> getDepositList(Long userId) {
		return depositRepository.findDepositsByUserId(userId);
	}
}