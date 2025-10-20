package com.opom.bankingapp.service.impl;

import com.opom.bankingapp.dto.user.AccountDetailResponse;

import com.opom.bankingapp.dto.scan.FromAccountTokenPayload;
import com.opom.bankingapp.dto.scan.GenerateFromAccountTokenRequest;
import com.opom.bankingapp.dto.scan.GenerateQrRequest;
import com.opom.bankingapp.dto.scan.GenerateQrResponse;
import com.opom.bankingapp.dto.scan.QrTokenPayload;

import com.opom.bankingapp.model.UserPrincipal;
import com.opom.bankingapp.repository.AccountRepository;
import com.opom.bankingapp.repository.UserRepository;
import com.opom.bankingapp.service.QrService;
import com.opom.bankingapp.service.TokenService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QrServiceImpl implements QrService {

    private final TokenService tokenService;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public QrServiceImpl(TokenService tokenService, AccountRepository accountRepository, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public GenerateQrResponse generateQrToken(UserPrincipal user, GenerateQrRequest request) {

        int toAccountId;

        Optional<Integer> selectedAccountIdOpt = userRepository.findSelectedAccountIdByUserId(user.getId());

        if (selectedAccountIdOpt.isPresent()) {
            toAccountId = selectedAccountIdOpt.get();
        } else {
            List<AccountDetailResponse> accounts = accountRepository.findAccountsByUserId(user.getId());
            if (accounts.isEmpty()) {
                throw new BadCredentialsException("User has no account");
            }
            toAccountId = accounts.get(0).id();
        }

        QrTokenPayload payload = new QrTokenPayload(
                toAccountId,
                request.amount(),
                request.note()
        );

        String token = tokenService.encode(payload, 86400000L/*300000L*/); // 5 minutes

        return new GenerateQrResponse(token);
    }

    @Override
    public GenerateQrResponse generateFromAccountToken(UserPrincipal user, GenerateFromAccountTokenRequest request) {
        boolean accountMatchesUser = accountRepository.findAccountsByUserId(user.getId())
                .stream()
                .anyMatch(acc -> acc.id() == request.fromAccountId());

        if (!accountMatchesUser) {
            throw new BadCredentialsException("Account not found or does not belong to user");
        }

        FromAccountTokenPayload payload = new FromAccountTokenPayload(request.fromAccountId());

        String token = tokenService.encode(payload, 86400000L); // 24 * 60 * 60 * 1000 = 86,400,000ms (1 day)

        return new GenerateQrResponse(token);
    }
}
