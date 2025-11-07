package com.opom.bankingapp.controller.personalbanking;

import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.dto.transfer.*;
import com.opom.bankingapp.model.UserPrincipal;
import com.opom.bankingapp.service.TransactionService;
import com.opom.bankingapp.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personal-banking/transfer")
public class TransferController {

    private final TransferService transferService;
    private final TransactionService transactionService;

    public TransferController(TransferService transferService, TransactionService transactionService) {
        this.transferService = transferService;
        this.transactionService = transactionService;
    }

    @PostMapping("/nickname/prepare")
    public ResponseEntity<ApiResponse<TransferPrepareResponse>> prepareNicknameTransfer(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody NicknamePrepareRequest request) {
        
        TransferPrepareResponse responseData = transferService.prepareNicknameTransfer(user.getId(), request);

        ApiResponse<TransferPrepareResponse> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            "Transfer prepared successfully",
            responseData
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/to-account-number/prepare")
    public ResponseEntity<ApiResponse<TransferPrepareResponse>> prepareAccountNumberTransfer(
            @RequestBody AccountNumberPrepareRequest request) {

        TransferPrepareResponse responseData = transferService.prepareAccountNumberTransfer(request);

        ApiResponse<TransferPrepareResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Account details retrieved successfully",
                responseData
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<ValidateTransferResponse>> validateTransfer(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody ValidateTransferRequest request) {

        ValidateTransferResponse responseData = transferService.validateTransfer(user.getId(), request);

        ApiResponse<ValidateTransferResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Transfer validated successfully",
                responseData
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<String>> confirmTransfer(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody ConfirmTransferRequest request) {

        transactionService.executeTransfer(user.getId(), request);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Transfer successful", null)
        );
    }
}
