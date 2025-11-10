package com.opom.bankingapp.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.opom.bankingapp.dto.scan.*;
import com.opom.bankingapp.dto.transfer.ValidateTransferRequest;
import com.opom.bankingapp.dto.transfer.ValidateTransferResponse;
import com.opom.bankingapp.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.dto.user.AccountDetailResponse;
import com.opom.bankingapp.model.UserPrincipal;
import com.opom.bankingapp.repository.AccountRepository;
import com.opom.bankingapp.repository.UserRepository;
import com.opom.bankingapp.service.QrService;
import com.opom.bankingapp.service.TokenService;
import com.opom.bankingapp.sse.service.SseEmitterService;

@Service
public class QrServiceImpl implements QrService {

    private final TokenService tokenService;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final SseEmitterService sseEmitterService;
    private final TransferService transferService;
    
    public QrServiceImpl(TokenService tokenService, AccountRepository accountRepository, UserRepository userRepository,
    					SseEmitterService sseEmitterService, TransferService transferService) {
        this.tokenService = tokenService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.sseEmitterService = sseEmitterService;
        this.transferService = transferService;
    }

    @Override
    public GenerateQrResponse generateQrToken(UserPrincipal user, GenerateQrRequest request) {

        long toAccountId;

        Optional<Long> selectedAccountIdOpt = userRepository.findSelectedAccountIdByUserId(user.getId());

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
    public GenerateQrResponse generateFromAccountToken(UserPrincipal user/*, GenerateFromAccountTokenRequest request*/) {
        Optional<Long> selectedAccountIdOpt = userRepository.findSelectedAccountIdByUserId(user.getId());

        if (selectedAccountIdOpt.isEmpty()) {
            throw new BadCredentialsException("User has no account");
        }
        final Long selectedAccountId = selectedAccountIdOpt.get();
        boolean accountMatchesUser = accountRepository.findAccountsByUserId(user.getId())
                .stream()
                .anyMatch(acc -> acc.id() == selectedAccountId);

        if (!accountMatchesUser) {
            throw new BadCredentialsException("Account not found or does not belong to user");
        }

        FromAccountTokenPayload payload = new FromAccountTokenPayload(selectedAccountId);
        String token = tokenService.encode(payload, 86400000L); // 24 * 60 * 60 * 1000 = 86,400,000ms (1 day)
        return new GenerateQrResponse(token);
    }

    @Override
    public SseEmitter subscribeTopic(String topic) {
        Optional<FromAccountTokenPayload> payloadOpt = tokenService.decode(topic, FromAccountTokenPayload.class);
        if (payloadOpt.isEmpty()) {
            throw new BadCredentialsException("Invalid or expired topic token");
        }
        return sseEmitterService.createEmitter(topic);
    }

	@Override
	public QrToReceiveResponse handleQrToReceiveScan(UserPrincipal user,ScannedQrRequest request) {
        Optional<QrTokenPayload> payloadOpt = tokenService.decode(request.getToken(),QrTokenPayload.class);

        QrTokenPayload payload = payloadOpt.orElseThrow(() -> new BadCredentialsException("Invalid or expired topic token"));

        QrToReceiveResponse responseData = null;
        if (payload.toAccountId() != null) {
             responseData = new QrToReceiveResponse(
                    transferService.validateTransfer(
                            user.getId(),
                            new ValidateTransferRequest(payload.toAccountId().intValue())
                    ),
                    payload.amount(),
                    payload.note()
            );
        }

      return responseData;

	}

    @Override
    public void handleQrToPayScan(UserPrincipal user, ScannedQrRequest request) {
        Optional<FromAccountTokenPayload> payloadOpt = tokenService.decode(request.getToken(),FromAccountTokenPayload.class);

        payloadOpt.orElseThrow(() -> new BadCredentialsException("Invalid or expired topic token"));

        long toAccountId = 0;

        Optional<Long> selectedAccountIdOpt = userRepository.findSelectedAccountIdByUserId(user.getId());

        if (selectedAccountIdOpt.isPresent()) {
            toAccountId = selectedAccountIdOpt.get();
        }

        Map<String, Object> data = Map.of("toAccountId", toAccountId);
        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Scanned successfully",
                data);
        sseEmitterService.broadcast(request.getToken(), response);
    }
}
