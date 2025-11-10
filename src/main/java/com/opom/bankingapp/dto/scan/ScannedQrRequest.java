package com.opom.bankingapp.dto.scan;

public class ScannedQrRequest {
    private String token;

    public String getToken() {
        return token;
    }

    public ScannedQrRequest(String token) {
        this.token = token;
    }
}
