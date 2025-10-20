package com.opom.bankingapp.service;

import com.opom.bankingapp.dto.scan.GenerateFromAccountTokenRequest;
import com.opom.bankingapp.dto.scan.GenerateQrRequest;
import com.opom.bankingapp.dto.scan.GenerateQrResponse;
import com.opom.bankingapp.model.UserPrincipal;

public interface QrService {
    GenerateQrResponse generateQrToken(UserPrincipal user, GenerateQrRequest request);

    GenerateQrResponse generateFromAccountToken(UserPrincipal user, GenerateFromAccountTokenRequest request);
}
