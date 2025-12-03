package com.opom.bankingapp.model;

import lombok.Getter;

@Getter
public enum TransactionType {
    DEPOSIT(1, "Deposit"),
    WITHDRAWL(2, "Withdrawl"),
    TRANSFER(3, "Transfer");

    private final int code;
    private final String value;

    TransactionType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static TransactionType fromCode(int code) {
        for (TransactionType status : TransactionType.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid TransactionType code: " + code);
    }
}
