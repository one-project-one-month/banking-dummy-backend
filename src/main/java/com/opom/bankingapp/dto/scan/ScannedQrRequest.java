package com.opom.bankingapp.dto.scan;

public class ScannedQrRequest {
    private String token;

    public ScannedQrRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ScannedQrRequest(String token) {
        this.token = token;
    }
}
