package com.opom.bankingapp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum TransactionCategory {
    PAYROLL(1, "payroll"),
    ANNUAL_SALARY_PAYMENT(2, "annual salary payment"),
    MERCHANT_PAYMENT(3, "merchant payment"),
    GENERAL(0, "general");

    private final int code;
    private final String value;

    TransactionCategory(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static TransactionCategory fromCode(int code) {
        for (TransactionCategory category : values()) {
            if (category.code == code) {
                return category;
            }
        }
        return GENERAL;
    }

    @JsonCreator
    public static TransactionCategory fromValue(String value) {
        for (TransactionCategory category : values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown transaction category: " + value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}