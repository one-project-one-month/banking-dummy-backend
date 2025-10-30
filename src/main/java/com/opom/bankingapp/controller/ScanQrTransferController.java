package com.opom.bankingapp.controller;

import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.dto.scan.ScanToPayRequest;
import com.opom.bankingapp.model.UserPrincipal;
import com.opom.bankingapp.service.QrService;
import com.opom.bankingapp.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personal-banking/scan")
public class ScanQrTransferController {
    private final TransactionService transactionService;

    public ScanQrTransferController(QrService qrService, TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/qr-to-pay/transfer")
    public ResponseEntity<ApiResponse<String>> scanToPayTransfer(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody ScanToPayRequest request) {

        transactionService.executeScanToPay(user.getId(), request);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Transfer successful", null)
        );
    }
}
