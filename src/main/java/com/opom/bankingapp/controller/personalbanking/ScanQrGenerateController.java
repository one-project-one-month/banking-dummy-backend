package com.opom.bankingapp.controller.personalbanking;

import com.opom.bankingapp.dto.scan.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.model.UserPrincipal;
import com.opom.bankingapp.service.QrService;

@RestController
@RequestMapping("/personal-banking/scan")
public class ScanQrGenerateController {

    private final QrService qrService;
    public ScanQrGenerateController(QrService qrService) {
        this.qrService = qrService;
    }

    @PostMapping("/qr-to-receive/generate")
    public ResponseEntity<ApiResponse<GenerateQrResponse>> generateQr(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody GenerateQrRequest request) {

        GenerateQrResponse responseData = qrService.generateQrToken(user, request);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "QR token generated", responseData)
        );
    }

    @PostMapping("/qr-to-pay/generate")
    public ResponseEntity<ApiResponse<GenerateQrResponse>> generateFromAccountToken(
            @AuthenticationPrincipal UserPrincipal user
            /*@RequestBody GenerateFromAccountTokenRequest request*/) {

        GenerateQrResponse responseData = qrService.generateFromAccountToken(user/*, request*/);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Scan-to-receive token generated", responseData)
        );
    }
    
    @GetMapping("/qr-to-pay/subscribe")
    public SseEmitter subscribe(@RequestParam String token) {
        return qrService.subscribeTopic(token);
    }
    
    @PostMapping("/qr-to-receive/scan")
    public ResponseEntity<ApiResponse<QrToReceiveResponse>> handleQrToReceiveScan(@RequestBody ScannedQrRequest request,@AuthenticationPrincipal UserPrincipal user) {
    	QrToReceiveResponse qrToReceiveResponse = qrService.handleQrToReceiveScan(user,request);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Scanned!!!", qrToReceiveResponse)
        );
    }

    @PostMapping("/qr-to-pay/scan")
    public ResponseEntity<ApiResponse<Boolean>> handleQrToPayScan(@RequestBody ScannedQrRequest request,@AuthenticationPrincipal UserPrincipal user) {
        qrService.handleQrToPayScan(user,request);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Scanned!!!", true)
        );
    }

}
