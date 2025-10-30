package com.opom.bankingapp.dto.scan;

public record ScanToPayRequest(
    String token,
    String pin
) {}
