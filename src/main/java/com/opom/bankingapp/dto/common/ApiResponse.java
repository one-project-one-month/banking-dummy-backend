package com.opom.bankingapp.dto.common;

public record ApiResponse<T>(
    int code,
    String message,
    T data
) {}
