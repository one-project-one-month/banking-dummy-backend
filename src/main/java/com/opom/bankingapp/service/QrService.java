package com.opom.bankingapp.service;

import com.opom.bankingapp.dto.scan.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.opom.bankingapp.model.UserPrincipal;

public interface QrService {
    GenerateQrResponse generateQrToken(UserPrincipal user, GenerateQrRequest request);

    GenerateQrResponse generateFromAccountToken(UserPrincipal user/*, GenerateFromAccountTokenRequest request*/);
    
    SseEmitter subscribeTopic(String topic);

    QrToReceiveResponse handleQrToReceiveScan(UserPrincipal user, ScannedQrRequest request);

    void handleQrToPayScan(UserPrincipal user, ScannedQrRequest request);
}
